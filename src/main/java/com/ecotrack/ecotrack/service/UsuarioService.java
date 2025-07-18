package com.ecotrack.ecotrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecotrack.ecotrack.dto.EditUsuarioDTO;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.repository.UsuarioRepositorio;

import jakarta.persistence.EntityNotFoundException;

public interface UsuarioService {

    // Create (Registration)
    public Usuario registrar(Usuario usuario);

    // Read All
    public List<Usuario> listarTodos();

    // Read by ID
    public Usuario encontrarPorId(Long id);

    // Read by Email
    public Optional<Usuario> encontrarPorEmail(String email);
    // Update
    public Usuario actualizar(Usuario usuario);

    // Delete
    public void eliminar(Long id);
    
    public Usuario getCurrentUser();

    // Update self-profile (limited fields, no role/activo changes)
    public Usuario actualizarPerfil(EditUsuarioDTO dto);

    // Update any user (for admins, allows all fields including role/activo)
    public Usuario actualizarUsuarioAdmin(Usuario updatedUsuario);
}
