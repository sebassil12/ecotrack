package com.ecotrack.ecotrack.controller;


import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecotrack.ecotrack.model.Medalla;
import com.ecotrack.ecotrack.service.impl.MedallaServiceImpl;

@Controller
@RequestMapping("/medallas")
public class MedallaController {

    @Autowired
    private MedallaServiceImpl medallaService;

    // Listar todas las medallas
    @GetMapping
    public String listMedallas(Model model) {
        model.addAttribute("medallas", medallaService.findAll());
        return "medallas-list"; // Plantilla para la lista
    }

    // Mostrar formulario para crear nueva medalla
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("medalla", new Medalla());
        model.addAttribute("tiposMedalla", Medalla.TipoMedalla.values());
        return "medalla-form"; // Plantilla para creación
    }

    // Mostrar formulario para editar medalla existente (ahora usa template separado)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Medalla> optionalMedalla = medallaService.findById(id);
        if (optionalMedalla.isPresent()) {
            model.addAttribute("medalla", optionalMedalla.get());
            model.addAttribute("tiposMedalla", Medalla.TipoMedalla.values());
            return "medalla-edit"; // Plantilla específica para edición
        } else {
            redirectAttributes.addFlashAttribute("error", "Medalla no encontrada con ID: " + id);
            return "redirect:/medallas";
        }
    }

    // Guardar o actualizar medalla (maneja tanto create como update)
    @PostMapping
    public String saveMedalla(@Valid @ModelAttribute Medalla medalla, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tiposMedalla", Medalla.TipoMedalla.values());
            // Retorna al template correspondiente basado en si es edición o creación
            if (medalla.getId() != null) {
                return "medalla-edit";
            } else {
                return "medalla-form";
            }
        }
        medallaService.save(medalla);
        redirectAttributes.addFlashAttribute("success", "Medalla guardada exitosamente.");
        return "redirect:/medallas";
    }

    // Eliminar medalla
    @PostMapping("/delete/{id}")
    public String deleteMedalla(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Medalla> optionalMedalla = medallaService.findById(id);
        if (optionalMedalla.isPresent()) {
            medallaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Medalla eliminada exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Medalla no encontrada con ID: " + id);
        }
        return "redirect:/medallas";
    }
}