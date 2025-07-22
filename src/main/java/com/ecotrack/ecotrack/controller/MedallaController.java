package com.ecotrack.ecotrack.controller;


import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.repository.UsuarioMedallaRepository;
import com.ecotrack.ecotrack.service.impl.MedallaServiceImpl;
import com.ecotrack.ecotrack.service.impl.UsuarioServiceImpl;

@Controller
@RequestMapping("/medallas")
public class MedallaController {

    @Autowired
    private MedallaServiceImpl medallaService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private UsuarioMedallaRepository usuarioMedallaRepository;

    // Listar todas las medallas
    @GetMapping
    public String listMedallas(Model model) {
        List<Medalla> medallas = medallaService.findAll();
        model.addAttribute("medallas", medallas);

        // Step 3.2: Get current user
        Usuario currentUser = usuarioService.getCurrentUser();
        if (currentUser == null) {
            // Handle unauthenticated (e.g., redirect to login)
            return "redirect:/login";
        }

        // Step 3.3: Fetch achieved medal IDs for the user
        Set<Long> achievedMedalIds = usuarioMedallaRepository.findByUsuario(currentUser)
            .stream()
            .map(um -> um.getMedalla().getId())
            .collect(Collectors.toSet());

        model.addAttribute("achievedMedalIds", achievedMedalIds);
        model.addAttribute("usuarioNombre", currentUser.getNombre()); // Or whatever field holds the display name

        return "medallas-list";
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