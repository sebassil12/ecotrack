package com.ecotrack.ecotrack.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecotrack.ecotrack.dto.PuntoVerdeDTO;
import com.ecotrack.ecotrack.dto.PuntoVerdeRequest;
import com.ecotrack.ecotrack.model.PuntoVerde;
import com.ecotrack.ecotrack.service.impl.PuntoVerdeServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/puntos-verdes")
@RequiredArgsConstructor
public class PuntoVerdeControlador {

    private final PuntoVerdeServiceImpl service;
    private String API = "/api/puntos-verdes";
    
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        // Create an "empty" record with defaults
        PuntoVerdeRequest emptyRequest = new PuntoVerdeRequest(
            "",  // Empty string for nombre (will trigger @NotBlank validation on submit if not filled)
            "",  // Empty string for descripcion (optional)
            BigDecimal.ZERO,  // Default for latitud (valid within -90 to 90)
            BigDecimal.ZERO   // Default for longitud (valid within -180 to 180)
        );
        model.addAttribute("puntoRequest", emptyRequest);
        return "registrar-punto-verde";
    }
    
    @PostMapping("/registrar")
    public String registrarPunto(@Valid @ModelAttribute("puntoRequest") PuntoVerdeRequest request, 
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If validation fails, redisplay the form with errors
            return "registrar-punto-verde";
        }
        
        // Call your service to save (similar to your API POST)
       service.registrar(request);
        
        // Optionally, add success message or redirect to the map view
        model.addAttribute("mensaje", "Punto verde registrado exitosamente.");
        
        String completeURL= API + "/mapa-todos"; 
        return "redirect:" + completeURL; // Redirect to your existing all-points map
    }

    @GetMapping("/mapa-todos") // Nueva URL para ver todos los puntos
    public String verMapaDeTodos(Model model) {
        List<PuntoVerde> puntosEntities = service.listarTodos(); // Assuming your repo

        // Map to DTOs to avoid entity cycles and lazy loading issues
        List<PuntoVerdeDTO> puntosDTOs = puntosEntities.stream()
            .map(p -> new PuntoVerdeDTO(p.getNombre(), p.getDireccion(), p.getLatitud(), p.getLongitud()))
            .collect(Collectors.toList());

        model.addAttribute("puntos", puntosDTOs);
        // Add other attributes like usuarioNombre if needed
        return "mapa-todos"; // Usaremos una nueva plantilla
    }
    
    @GetMapping("/lista")
    public String mostrarLista(Model model) {
        List<PuntoVerde> puntos = service.listarTodos();
        model.addAttribute("puntos", puntos);
        return "lista-puntos-verdes"; // Thymeleaf template name
    }
    
    @PostMapping("/eliminar")
    public String eliminarPunto(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.eliminarPunto(id);
            redirectAttributes.addFlashAttribute("mensaje", "Punto verde eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        
        String completeURL = API + "/lista";
        return "redirect:" + completeURL; // Redirect back to the list
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PuntoVerde punto = service.buscarPorId(id); // Assume service has obtenerPorId(Long id)
            if (punto == null) {
                redirectAttributes.addFlashAttribute("error", "Punto verde no encontrado.");
                return "redirect:" + API + "/lista";
            }
            
            // Convert PuntoVerde to PuntoVerdeRequest for form binding
            PuntoVerdeRequest request = new PuntoVerdeRequest(
                punto.getNombre(),
                punto.getDescripcion(),
                punto.getLatitud(),
                punto.getLongitud()
            );
            
            model.addAttribute("puntoRequest", request);
            model.addAttribute("id", id); // Pass ID for form action
            return "editar-punto-verde"; // Thymeleaf template for edit form
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el punto verde: " + e.getMessage());
            return "redirect:" + API + "/lista";
        }
    }

    // New POST endpoint to handle edit submission
    @PostMapping("/editar/{id}")
    public String editarPunto(@PathVariable Long id, 
                              @Valid @ModelAttribute("puntoRequest") PuntoVerdeRequest request, 
                              BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // If validation fails, redisplay the form with errors
            model.addAttribute("id", id); // Retain ID for form action
            return "editar-punto-verde";
        }
        
        try {
            service.actualizar(id, request); // Assume service has actualizar(Long id, PuntoVerdeRequest request)
            redirectAttributes.addFlashAttribute("mensaje", "Punto verde actualizado exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar: " + e.getMessage());
            model.addAttribute("id", id); // Retain ID for form action
            return "editar-punto-verde";
        }
        
        String completeURL = API + "/lista"; 
        return "redirect:" + completeURL; // Redirect back to the list
    }
}
