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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Usuario usuario = usuarioService.getCurrentUser();

        // Step 2.2: Get current authentication and check if RECOLECTOR
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isRecolector = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("RECOLECTOR"));

        // Step 2.3: Fetch recolecciones based on role
        List<Recoleccion> recolecciones;
        if (isRecolector) {
            recolecciones = recoleccionService.getAll();  // All recolecciones for recolectors
        } else {
            recolecciones = recoleccionService.getAllByUsuario(usuario);  // Only current user's for others
        }

        // Step 2.4: Add to model (as before)
        model.addAttribute("recolecciones", recolecciones);
        model.addAttribute("nuevaRecoleccion", new Recoleccion());
        setupDropdowns(model);  // Assuming this populates dropdowns like usuarios, tiposResiduo, etc.
        return "recolecciones";
    }

    @PostMapping
    public String registrarRecoleccion(@ModelAttribute("nuevaRecoleccion") Recoleccion nuevaRecoleccion) {
        // Step 3.1: Get current authentication and check role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isRecolector = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("RECOLECTOR"));

        // Step 3.2: Enforce validado restriction
        if (!isRecolector) {
            nuevaRecoleccion.setValidado(false); // Override to false for non-RECOLECTOR
        }

        // Step 3.3: Proceed with saving
        recoleccionService.registrarRecoleccion(nuevaRecoleccion);
        return "redirect:/recolecciones";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recoleccion recoleccion = recoleccionService.getById(id);
        model.addAttribute("editRecoleccion", recoleccion);
        setupDropdowns(model); // No changes here—logic moves inside setupDropdowns
        return "edit-recoleccion";
    }

    @PostMapping("/update/{id}")
    public String updateRecoleccion(@PathVariable Long id, @ModelAttribute("editRecoleccion") Recoleccion updatedRecoleccion) {
        // No need to load existingRecoleccion here if you're going to pass updatedRecoleccion directly
        // and let the service handle the merging.
        // However, if you have specific logic in the controller for related entities,
        // you might keep it, but be aware of the persistence context issue.

        // A cleaner approach is to pass 'updatedRecoleccion' and the ID to the service
        // and let the service manage loading and merging.

        // Example of passing updatedRecoleccion directly:
        // recoleccionService.updateRecoleccion(id, updatedRecoleccion);

        // If you need to handle related entities in the controller before passing:
        Recoleccion existingRecoleccion = recoleccionService.getById(id); // Still loading here for related entities

        // Update simple fields from the form data (updatedRecoleccion) to the existing one
        existingRecoleccion.setCantidad(updatedRecoleccion.getCantidad());
        existingRecoleccion.setObservaciones(updatedRecoleccion.getObservaciones());
        // Crucially, DO NOT set validado here in the controller directly from updatedRecoleccion if the service
        // needs to check the 'before' state.
        // Instead, let the service handle the 'validado' change based on its internal logic.
        // If 'validado' is coming from the form, you should pass it, but the service needs
        // to compare it to the DB's current state.

        // Handle related entities based on IDs, as these are typically independent updates
        if (updatedRecoleccion.getUsuario() != null && updatedRecoleccion.getUsuario().getId() != null) {
            Usuario usuario = usuarioService.encontrarPorId(updatedRecoleccion.getUsuario().getId());
            existingRecoleccion.setUsuario(usuario);
        }
        if (updatedRecoleccion.getTipoResiduo() != null && updatedRecoleccion.getTipoResiduo().getId() != null) {
            TipoResiduo tipoResiduo = tipoResiduoService.getById(updatedRecoleccion.getTipoResiduo().getId());
            existingRecoleccion.setTipoResiduo(tipoResiduo);
        }
        if (updatedRecoleccion.getPuntoVerde() != null && updatedRecoleccion.getPuntoVerde().getId() != null) {
            PuntoVerde puntoVerde = puntoVerdeServicio.buscarPorId(updatedRecoleccion.getPuntoVerde().getId());
            existingRecoleccion.setPuntoVerde(puntoVerde);
        }

        // Pass the original updatedRecoleccion to the service, or pass specific fields
        // that should be used for the 'validado' logic.
        // The most robust way is to pass 'updatedRecoleccion' directly and let the service merge.
        recoleccionService.updateRecoleccion(id, updatedRecoleccion); // Pass updatedRecoleccion directly
        return "redirect:/recolecciones";
    }
    @PostMapping("/delete/{id}")
    public String deleteRecoleccion(@PathVariable Long id) {
        recoleccionService.deleteRecoleccion(id);
        return "redirect:/recolecciones";
    }

    private void setupDropdowns(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isRecolector = authentication != null && authentication.isAuthenticated() &&
            authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("RECOLECTOR"));

        List<Usuario> usuarios = new ArrayList<>(); // Inicializa la lista aquí para simplicidad
        if (isRecolector) {
            usuarios = usuarioService.listarTodos(); // Todos los usuarios para recolectores
        } else {
            Usuario currentUser = usuarioService.getCurrentUser();
            if (currentUser != null) { // Chequeo para evitar null
                usuarios.add(currentUser); // Solo el usuario actual para no recolectores
            }
            // Opcional: Si quieres manejar el caso de null (e.g., no autenticado), podrías agregar un log o un usuario fallback, pero no es estrictamente necesario.
        }
        model.addAttribute("usuarios", usuarios); // Agrega la lista (nunca un objeto único)
        
        // Resto de dropdowns (sin cambios)
        model.addAttribute("tiposResiduo", tipoResiduoService.getAll());
        model.addAttribute("puntosVerdes", puntoVerdeServicio.listarTodos()); // Descomenta si es necesario
    }
}