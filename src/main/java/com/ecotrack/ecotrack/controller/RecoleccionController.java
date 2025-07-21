package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.model.Recoleccion;
import com.ecotrack.ecotrack.model.TipoResiduo;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.model.PuntoVerde; // If needed
import com.ecotrack.ecotrack.service.impl.RecoleccionServiceImpl;
import com.ecotrack.ecotrack.service.impl.UsuarioServiceImpl; // Assume exists
import com.ecotrack.ecotrack.service.impl.TipoResiduoServiceImpl; // Assume exists
import com.ecotrack.ecotrack.service.impl.PuntoVerdeServiceImpl; // Assume exists if needed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recolecciones")
public class RecoleccionController {

    @Autowired
    private RecoleccionServiceImpl recoleccionService;

    @Autowired
    private UsuarioServiceImpl usuarioService; // For dropdowns

    @Autowired
    private TipoResiduoServiceImpl tipoResiduoService; // For dropdowns

    @Autowired 
    private PuntoVerdeServiceImpl puntoVerdeServicio; // Uncomment if needed

    @GetMapping
    public String showRecolecciones(Model model) {
        Usuario usuario = usuarioService.getCurrentUser(); // For dropdowns
        List<Recoleccion> recolecciones = recoleccionService.getAllByUsuario(usuario);
        model.addAttribute("recolecciones", recolecciones);
        model.addAttribute("nuevaRecoleccion", new Recoleccion());
        setupDropdowns(model);
        return "recolecciones";
    }

    @PostMapping
    public String registrarRecoleccion(@ModelAttribute("nuevaRecoleccion") Recoleccion nuevaRecoleccion) {
        recoleccionService.registrarRecoleccion(nuevaRecoleccion);
        return "redirect:/recolecciones";
    }

    // CAMBIOS AQUÍ: Retorna la nueva plantilla y prepara solo lo necesario para edición
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recoleccion recoleccion = recoleccionService.getById(id);
        model.addAttribute("editRecoleccion", recoleccion);
        setupDropdowns(model); // Prepara los dropdowns (usuarios, tiposResiduo, puntosVerdes)
        return "edit-recoleccion"; // Nueva plantilla
    }

    @PostMapping("/update/{id}")
    public String updateRecoleccion(@PathVariable Long id, @ModelAttribute("editRecoleccion") Recoleccion updatedRecoleccion) {
        // Cargar la recolección existente para evitar sobrescribir campos no editados
        Recoleccion existingRecoleccion = recoleccionService.getById(id);
        
        // Actualizar campos simples
        existingRecoleccion.setCantidad(updatedRecoleccion.getCantidad());
        existingRecoleccion.setValidado(updatedRecoleccion.getValidado());
        existingRecoleccion.setObservaciones(updatedRecoleccion.getObservaciones());
        // ... otros campos simples que edites
        
        // Actualizar entidades relacionadas basadas en IDs
        if (updatedRecoleccion.getUsuario() != null && updatedRecoleccion.getUsuario().getId() != null) {
            Usuario usuario = usuarioService.encontrarPorId(updatedRecoleccion.getUsuario().getId()); // Asume que tienes un método getById en UsuarioService
            existingRecoleccion.setUsuario(usuario);
        }
        if (updatedRecoleccion.getTipoResiduo() != null && updatedRecoleccion.getTipoResiduo().getId() != null) {
            TipoResiduo tipoResiduo = tipoResiduoService.getById(updatedRecoleccion.getTipoResiduo().getId()); // Asume método getById
            existingRecoleccion.setTipoResiduo(tipoResiduo);
        }
        if (updatedRecoleccion.getPuntoVerde() != null && updatedRecoleccion.getPuntoVerde().getId() != null) {
            PuntoVerde puntoVerde = puntoVerdeServicio.buscarPorId(updatedRecoleccion.getPuntoVerde().getId()); // Asume método getById
            existingRecoleccion.setPuntoVerde(puntoVerde);
        }
        
        // Guardar la recolección actualizada
        recoleccionService.updateRecoleccion(id, existingRecoleccion);
        return "redirect:/recolecciones";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecoleccion(@PathVariable Long id) {
        recoleccionService.deleteRecoleccion(id);
        return "redirect:/recolecciones";
    }

    private void setupDropdowns(Model model) {
        model.addAttribute("usuarios", usuarioService.getCurrentUser());
        model.addAttribute("tiposResiduo", tipoResiduoService.getAll());
        model.addAttribute("puntosVerdes", puntoVerdeServicio.listarTodos()); // Uncomment if needed
    }
}