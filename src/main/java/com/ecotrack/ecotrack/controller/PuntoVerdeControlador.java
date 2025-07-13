package com.ecotrack.ecotrack.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecotrack.ecotrack.dto.PuntoVerdeRequest;
import com.ecotrack.ecotrack.dto.PuntoVerdeResponse;
import com.ecotrack.ecotrack.model.PuntoVerde;
import com.ecotrack.ecotrack.service.impl.PuntoVerdeServicioImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/puntos-verdes")
@RequiredArgsConstructor
public class PuntoVerdeControlador {

    private final PuntoVerdeServicioImpl service;
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
        PuntoVerdeResponse response = service.registrar(request);
        
        // Optionally, add success message or redirect to the map view
        model.addAttribute("mensaje", "Punto verde registrado exitosamente.");
        
        String completeURL= API + "/mapa-todos"; 
        return "redirect:" + completeURL; // Redirect to your existing all-points map
    }
    
    // Endpoint para la vista del mapa
    @GetMapping("/{id}/mapa")
    public String verMapa(@PathVariable Long id, Model model) {
        PuntoVerde punto = service.buscarPorId(id);
        model.addAttribute("latitud", punto.getLatitud());
        model.addAttribute("longitud", punto.getLongitud());
        model.addAttribute("direccion", punto.getDireccion());
        model.addAttribute("nombre", punto.getNombre());
        return "mapa";
    }

    @GetMapping("/mapa-todos") // Nueva URL para ver todos los puntos
    public String verMapaDeTodos(Model model) {
        // Suponiendo que tienes un método en tu servicio para listar todos
        List<PuntoVerde> puntos = service.listarTodos(); 
        model.addAttribute("puntos", puntos); // Pasamos la lista completa
        return "mapa-todos"; // Usaremos una nueva plantilla
    }
}
