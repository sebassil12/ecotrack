package com.ecotrack.ecotrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estadisticas") // Or integrate under /dashboard if you prefer
public class EstadisticasController {

    @GetMapping
    public String showEstadisticas(Model model) {
        // Optionally add any model attributes (e.g., dynamic embed URL if needed)
        model.addAttribute("supersetEmbedUrl", "http://localhost:8088/superset/explore/p/Zz0VEp8EYqd/?standalone=1&height=400"); // Use your embed URL; make it configurable
        return "estadisticas"; // Name of the new Thymeleaf template
    }
}