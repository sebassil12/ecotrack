package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.dto.EditUsuarioDTO;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.service.impl.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(Model model) {
        Usuario currentUser = usuarioService.getCurrentUser();
        EditUsuarioDTO dto = new EditUsuarioDTO();
        dto.setId(currentUser.getId());
        dto.setNombre(currentUser.getNombre());
        dto.setApellidos(currentUser.getApellidos());
        dto.setEmail(currentUser.getEmail());
        dto.setTelefono(currentUser.getTelefono());
        // dto.setPassword(""); // Leave blank
        
        model.addAttribute("editUsuario", dto); // Bind to DTO
        // Add username for navbar
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("usuarioNombre", auth.getName());
        return "perfil-editar";
    }

    @PostMapping("/editar")
    public String procesarEditar(@Valid @ModelAttribute("editUsuario") EditUsuarioDTO dto, 
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "perfil-editar"; // Redisplay with errors
        }
        try {
            usuarioService.actualizarPerfil(dto);
            model.addAttribute("mensaje", "Perfil actualizado exitosamente.");
            return "perfil-editar"; // Or "redirect:/dashboard" for redirect
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "perfil-editar";
        }
    }
}