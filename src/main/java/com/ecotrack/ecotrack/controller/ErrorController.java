package com.ecotrack.ecotrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String mostrarAccessDenied(Model model) {
        model.addAttribute("mensaje", "No tienes permisos para acceder a esta sección. Solo administradores permitidos.");
        return "error-admin";  // Renderiza error-admin.html
    }
}