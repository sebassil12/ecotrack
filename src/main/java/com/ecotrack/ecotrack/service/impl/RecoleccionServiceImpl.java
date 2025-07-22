package com.ecotrack.ecotrack.service.impl;

import com.ecotrack.ecotrack.model.Medalla;
import com.ecotrack.ecotrack.model.Recoleccion;
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
        Recoleccion existing = getById(id);
        // Update fields (manual merge; use BeanUtils if preferred)
        existing.setTipoResiduo(updatedRecoleccion.getTipoResiduo());
        existing.setCantidad(updatedRecoleccion.getCantidad());
        existing.setObservaciones(updatedRecoleccion.getObservaciones());
        existing.setPuntoVerde(updatedRecoleccion.getPuntoVerde());
        // Recalculate points
        existing.calcularPuntos();
        // Handle validation change
        if (!existing.getValidado() && updatedRecoleccion.getValidado()) {
            existing.setValidado(true);
            existing.setFechaValidacion(LocalDateTime.now());
            // Assuming validadoPor is provided in updatedRecoleccion
            existing.setValidadoPor(updatedRecoleccion.getValidadoPor());
            updateUserPoints(existing.getUsuario(), existing.getPuntos());
        } else if (existing.getValidado() && !updatedRecoleccion.getValidado()) {
            // Optional: Reverse points if unvalidating
            updateUserPoints(existing.getUsuario(), -existing.getPuntos());
            existing.setValidado(false);
            existing.setFechaValidacion(null);
            existing.setValidadoPor(null);
        }
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
        usuario.setPuntosTotal(usuario.getPuntosTotal() + puntosToAdd);
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