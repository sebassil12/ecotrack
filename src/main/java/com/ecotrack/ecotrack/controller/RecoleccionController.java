package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.model.Recoleccion;
import com.ecotrack.ecotrack.model.TipoResiduo;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.model.PuntoVerde; // If needed
import com.ecotrack.ecotrack.service.impl.RecoleccionService;
import com.ecotrack.ecotrack.service.impl.UsuarioService; // Assume exists
import com.ecotrack.ecotrack.service.impl.TipoResiduoService; // Assume exists
import com.ecotrack.ecotrack.service.impl.PuntoVerdeServicioImpl; // Assume exists if needed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recolecciones")
public class RecoleccionController {

    @Autowired
    private RecoleccionService recoleccionService;

    @Autowired
    private UsuarioService usuarioService; // For dropdowns

    @Autowired
    private TipoResiduoService tipoResiduoService; // For dropdowns

    // @Autowired private PuntoVerdeService puntoVerdeService; // Uncomment if needed

    @GetMapping
    public String showRecolecciones(Model model) {
        List<Recoleccion> recolecciones = recoleccionService.getAll();
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recoleccion recoleccion = recoleccionService.getById(id);
        model.addAttribute("editRecoleccion", recoleccion);
        setupDropdowns(model);
        // Redirect to main page but with modal open? For simplicity, we render the same template
        return "recolecciones"; // Or a separate edit template if preferred
    }

    @PostMapping("/update/{id}")
    public String updateRecoleccion(@PathVariable Long id, @ModelAttribute("editRecoleccion") Recoleccion updatedRecoleccion) {
        recoleccionService.updateRecoleccion(id, updatedRecoleccion);
        return "redirect:/recolecciones";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecoleccion(@PathVariable Long id) {
        recoleccionService.deleteRecoleccion(id);
        return "redirect:/recolecciones";
    }

    private void setupDropdowns(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("tiposResiduo", tipoResiduoService.getAll());
        // model.addAttribute("puntosVerdes", puntoVerdeService.getAll()); // Uncomment if needed
    }
}