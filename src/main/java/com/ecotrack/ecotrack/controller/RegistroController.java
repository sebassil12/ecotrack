package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.service.impl.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET: Show registration form
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario()); // Empty model for form binding
        return "registro"; // Thymeleaf template
    }

    // POST: Process registration
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario, 
                                   BindingResult bindingResult, 
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registro"; // Redisplay form with errors
        }
        try {
            usuarioService.registrar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Por favor, inicia sesión.");
            return "redirect:/login"; // Redirect to login
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "error.usuario", e.getMessage()); // e.g., duplicate email
            return "registro";
        }
    }
}