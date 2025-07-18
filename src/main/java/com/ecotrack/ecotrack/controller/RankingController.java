// RankingController.java
package com.ecotrack.ecotrack.controller; // Ajusta el paquete

import com.ecotrack.ecotrack.model.Ranking;
import com.ecotrack.ecotrack.service.impl.UsuarioServiceImpl; // Asume que existe
import com.ecotrack.ecotrack.service.impl.RankingServiceImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/rankings")
public class RankingController {

    @Autowired
    private RankingServiceImpl rankingService;

    @Autowired
    private UsuarioServiceImpl usuarioService; // Para obtener lista de usuarios

    // Listar todos los rankings
    @GetMapping
    public String listRankings(Model model) {
        model.addAttribute("rankings", rankingService.findAll());
        return "rankings-list";
    }

    // Mostrar formulario para crear nuevo ranking
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ranking", new Ranking());
        model.addAttribute("usuarios", usuarioService.listarTodos()); // Lista de usuarios para select
        return "ranking-form";
    }

    // Mostrar formulario para editar ranking existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ranking> optionalRanking = rankingService.findById(id);
        if (optionalRanking.isPresent()) {
            model.addAttribute("ranking", optionalRanking.get());
            model.addAttribute("usuarios", usuarioService.listarTodos()); // Lista de usuarios para select
            return "ranking-edit";
        } else {
            redirectAttributes.addFlashAttribute("error", "Ranking no encontrado con ID: " + id);
            return "redirect:/rankings";
        }
    }

    // Guardar o actualizar ranking (maneja tanto create como update)
    @PostMapping
    public String saveRanking(@Valid @ModelAttribute Ranking ranking, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listarTodos());
            // Retorna al template correspondiente basado en si es edición o creación
            if (ranking.getId() != null) {
                return "ranking-edit";
            } else {
                return "ranking-form";
            }
        }
        rankingService.save(ranking);
        redirectAttributes.addFlashAttribute("success", "Ranking guardado exitosamente.");
        return "redirect:/rankings";
    }

    // Eliminar ranking
    @PostMapping("/delete/{id}")
    public String deleteRanking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Ranking> optionalRanking = rankingService.findById(id);
        if (optionalRanking.isPresent()) {
            rankingService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Ranking eliminado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Ranking no encontrado con ID: " + id);
        }
        return "redirect:/rankings";
    }
}