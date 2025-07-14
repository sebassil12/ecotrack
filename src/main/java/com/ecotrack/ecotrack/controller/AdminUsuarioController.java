package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.service.impl.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasAuthority('ADMINISTRADOR')") // Extra security layer
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "listar-usuarios"; // Template name
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.encontrarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        model.addAttribute("usuario", usuario);
        return "editar-usuario"; // Template name
    }

    @PostMapping("/editar/{id}")
    public String procesarEditar(@PathVariable Long id, 
                                 @Valid @ModelAttribute("usuario") Usuario usuario, 
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editar-usuario";
        }
        try {
            usuario.setId(id); // Ensure ID matches
            usuarioService.actualizarUsuarioAdmin(usuario);
            model.addAttribute("mensaje", "Usuario actualizado exitosamente.");
            return "redirect:/admin/usuarios"; // Back to list
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "editar-usuario";
        }
    }
}