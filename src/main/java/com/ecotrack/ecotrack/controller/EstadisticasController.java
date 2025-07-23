package com.ecotrack.ecotrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticasController {

    // List of available Superset embed URLs (expand this as you add more charts)
    private static final List<String> CHART_URLS = List.of(
        "http://localhost:8088/superset/explore/p/1Zej8l2jrPM/?standalone=1&height=400",  // Chart 1
        "http://localhost:8088/superset/explore/p/aVyELRZNMdQ/?standalone=1&height=400"   // Chart 2
    );

    // Main menu of statistics (shows cards)
    @GetMapping
    public String showEstadisticasMenu(Model model) {
        // Pass the list of charts to the template (for rendering cards dynamically)
        model.addAttribute("charts", CHART_URLS);
        return "estadisticas-menu";  // New template for the menu (we'll create this below)
    }

    // View a specific statistic's full embed
    @GetMapping("/view/{id}")
    public String showEstadisticaDetalle(@PathVariable int id, Model model) {
        if (id < 1 || id > CHART_URLS.size()) {
            // Handle invalid ID (e.g., redirect or show error)
            return "redirect:/estadisticas";
        }
        // Pass the selected embed URL to the template
        model.addAttribute("supersetEmbedUrl", CHART_URLS.get(id - 1));  // IDs start from 1
        return "estadistica-detalle";  // New template for individual chart view (we'll create this below)
    }
}