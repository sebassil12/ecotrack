package com.ecotrack.ecotrack.controller; // Adjust package

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginControlador {

    @GetMapping("/login")
    public String login(Model model) {
        // Optional: Add any model attributes if needed
        return "login"; // Thymeleaf template name (login.html)
    }
    
    @PostMapping("/logout")
    public String logout() {
        // Spring Security will automatically handle session invalidation and auth clearance
        // Here, we just redirect to the login page with a logout param (for success message)
        return "redirect:/login?logout";
    }
}