package com.ecotrack.ecotrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard") // Base path for the dashboard
public class DashboardControlador {

    // If needed, inject services here (e.g., for user data)

    @GetMapping // Access at /dashboard
    public String mostrarDashboard(Model model) {
        // Optional: Add model attributes, e.g., current user for profile
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            model.addAttribute("usuarioNombre", authentication.getName()); // Example: Add username to model
//        } else {
//            model.addAttribute("usuarioNombre", "Invitado"); // Fallback
//        }
        
        return "dashboard"; // Thymeleaf template name (dashboard.html)
    }
}
