package com.ecotrack.ecotrack.service.impl;

import com.ecotrack.ecotrack.model.Medalla;
import com.ecotrack.ecotrack.model.PuntoVerde;
import com.ecotrack.ecotrack.model.Recoleccion;
import com.ecotrack.ecotrack.model.TipoResiduo;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.model.UsuarioMedalla;
import com.ecotrack.ecotrack.repository.MedallaRepository;
import com.ecotrack.ecotrack.repository.RecoleccionRepository;
import com.ecotrack.ecotrack.repository.UsuarioMedallaRepository;
import com.ecotrack.ecotrack.repository.UsuarioRepositorio; // Assume this exists
import com.ecotrack.ecotrack.service.RecoleccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecoleccionServiceImpl implements RecoleccionService {

    @Autowired
    private RecoleccionRepository recoleccionRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private MedallaRepository medallaRepository;

    @Autowired
    private UsuarioMedallaRepository usuarioMedallaRepository;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private TipoResiduoServiceImpl tipoResiduoService;

    @Autowired
    private PuntoVerdeServiceImpl puntoVerdeServicio;

    @Transactional
    public Recoleccion registrarRecoleccion(Recoleccion recoleccion) {
        recoleccion.calcularPuntos();
        Recoleccion saved = recoleccionRepository.save(recoleccion);
        if (saved.getValidado()) {
            updateUserPoints(saved.getUsuario(), saved.getPuntos());
        }
        return saved;
    }

    public Recoleccion getById(Long id) {
        return recoleccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recoleccion not found"));
    }

    @Transactional
    public Recoleccion updateRecoleccion(Long id, Recoleccion updatedRecoleccion) {
        // 1. Load the existing Recoleccion from the database.
        // This 'existing' object will reflect the current state in the DB before any changes from the form.
        Recoleccion existing = recoleccionRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Recoleccion not found with id: " + id));

        // 2. Store the original 'validado' status for comparison.
        boolean wasValidated = existing.getValidado();

        // 3. Update simple fields from the 'updatedRecoleccion' (from the form)
        // to the 'existing' entity (from the database).
        existing.setTipoResiduo(updatedRecoleccion.getTipoResiduo());
        existing.setCantidad(updatedRecoleccion.getCantidad());
        existing.setObservaciones(updatedRecoleccion.getObservaciones());
        existing.setPuntoVerde(updatedRecoleccion.getPuntoVerde());

        // 4. Update related entities based on IDs from updatedRecoleccion,
        // ensuring they are managed entities if they need to be.
        // NOTE: If your controller already handles setting these relations on a Recoleccion object
        // and passes that object, you might just copy those over.
        // However, it's often safer to fetch them again within the transaction if needed,
        // or ensure they are properly managed.
        if (updatedRecoleccion.getUsuario() != null && updatedRecoleccion.getUsuario().getId() != null) {
            Usuario usuario = usuarioService.encontrarPorId(updatedRecoleccion.getUsuario().getId());
            existing.setUsuario(usuario);
        }
        if (updatedRecoleccion.getTipoResiduo() != null && updatedRecoleccion.getTipoResiduo().getId() != null) {
            TipoResiduo tipoResiduo = tipoResiduoService.getById(updatedRecoleccion.getTipoResiduo().getId());
            existing.setTipoResiduo(tipoResiduo);
        }
        if (updatedRecoleccion.getPuntoVerde() != null && updatedRecoleccion.getPuntoVerde().getId() != null) {
            PuntoVerde puntoVerde = puntoVerdeServicio.buscarPorId(updatedRecoleccion.getPuntoVerde().getId());
            existing.setPuntoVerde(puntoVerde);
        }


        // 5. Recalculate points *before* handling validation changes, as validation might depend on points.
        existing.calcularPuntos();

        // 6. Handle validation change based on the *original* state and the *new* state.
        boolean willBeValidated = updatedRecoleccion.getValidado();

        if (!wasValidated && willBeValidated) {
            // Transition from NOT validated to VALIDATED
            existing.setValidado(true);
            existing.setFechaValidacion(LocalDateTime.now());
            existing.setValidadoPor(updatedRecoleccion.getValidadoPor());
            updateUserPoints(existing.getUsuario(), existing.getPuntos());
        } else if (wasValidated && !willBeValidated) {
            // Transition from VALIDATED to NOT validated
            // Optional: Reverse points if unvalidating
            updateUserPoints(existing.getUsuario(), -existing.getPuntos());
            existing.setValidado(false);
            existing.setFechaValidacion(null);
            existing.setValidadoPor(null);
        }
        // If wasValidated == willBeValidated (i.e., no change in validation status),
        // then no user points update related to validation status change is needed.

        return recoleccionRepository.save(existing);
    }

    @Transactional
    public void deleteRecoleccion(Long id) {
        Recoleccion existing = getById(id);
        if (existing.getValidado()) {
            // Reverse points on delete if validated
            updateUserPoints(existing.getUsuario(), -existing.getPuntos());
        }
        recoleccionRepository.deleteById(id);
    }

    @Transactional
    public Recoleccion validarRecoleccion(Long id, Usuario validadoPor) {
        Recoleccion recoleccion = getById(id);
        if (!recoleccion.getValidado()) {
            recoleccion.setValidado(true);
            recoleccion.setFechaValidacion(LocalDateTime.now());
            recoleccion.setValidadoPor(validadoPor);
            recoleccionRepository.save(recoleccion);
            updateUserPoints(recoleccion.getUsuario(), recoleccion.getPuntos());
        }
        return recoleccion;
    }

    private void updateUserPoints(Usuario usuario, Integer puntosToAdd) {
        Integer pointoToUPdate = usuario.getPuntosTotal() + puntosToAdd;
        if (pointoToUPdate < 0){
            usuario.setPuntosTotal(0);    
        } else {
            usuario.setPuntosTotal(usuario.getPuntosTotal() + puntosToAdd);
        }
        usuarioRepository.save(usuario);
        List<Medalla> activeMedals = medallaRepository.findByActivaTrueOrderByPuntosRequeridosAsc();

        // Step 2.3: Check each medal and award if eligible
        for (Medalla medalla : activeMedals) {
            if (usuario.getPuntosTotal() >= medalla.getPuntosRequeridos() &&
                !hasUserAchievedMedal(usuario, medalla)) {
                // Award the medal
                UsuarioMedalla usuarioMedalla = UsuarioMedalla.builder()
                    .usuario(usuario)
                    .medalla(medalla)
                    .fechaObtenida(LocalDateTime.now())
                    .puntosMomento(usuario.getPuntosTotal()) // Snapshot of points at award time
                    .build();
                usuarioMedallaRepository.save(usuarioMedalla);
            }
        }
    }

    private boolean hasUserAchievedMedal(Usuario usuario, Medalla medalla) {
        return usuarioMedallaRepository.existsByUsuarioAndMedalla(usuario, medalla);
    }

    public List<Recoleccion> getAll() {
        return recoleccionRepository.findAll();
    }

    public List<Recoleccion> getValidated() {
        return recoleccionRepository.findByValidado(true);
    }

    @Override
    public List<Recoleccion> getAllByUsuario(Usuario usuario) {
        return recoleccionRepository.findByUsuario(usuario); // Asume que tienes este método en el repository (agrega @Query si es necesario)
    }
}