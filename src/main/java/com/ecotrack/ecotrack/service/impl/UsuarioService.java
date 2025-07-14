package com.ecotrack.ecotrack.service.impl;

import com.ecotrack.ecotrack.dto.EditUsuarioDTO;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.repository.UsuarioRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepositorio usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create (Registration)
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Hash password
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setRol(Usuario.RolUsuario.CIUDADANO); // Default role; adjust if needed
        usuario.setPuntosTotal(0);
        usuario.setNivel(1);
        return usuarioRepository.save(usuario);
    }

    // Read All
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Read by ID
    public Optional<Usuario> encontrarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Read by Email
    public Optional<Usuario> encontrarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Update
    public Usuario actualizar(Usuario usuario) {
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        // Optionally re-hash password if changed
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuario.setFechaActualizacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    // Delete
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
    
    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado.");
        }
        String email = authentication.getName(); // Email as username
        return encontrarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    // Update self-profile (limited fields, no role/activo changes)
    public Usuario actualizarPerfil(EditUsuarioDTO dto) {
    	Usuario current = getCurrentUser();
        if (!current.getId().equals(dto.getId())) {
            throw new RuntimeException("No puedes editar perfiles de otros usuarios.");
        }
        // Map DTO to entity, preserving non-editable fields
        current.setNombre(dto.getNombre());
        current.setApellidos(dto.getApellidos());
        current.setTelefono(dto.getTelefono());
        current.setEmail(dto.getEmail()); // Allow if admin, but for self-edit, it should match current
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            current.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        current.setFechaActualizacion(LocalDateTime.now());
        return usuarioRepository.save(current);
    }

    // Update any user (for admins, allows all fields including role/activo)
    public Usuario actualizarUsuarioAdmin(Usuario updatedUsuario) {
        Usuario existing = encontrarPorId(updatedUsuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        // Update all fields
        existing.setNombre(updatedUsuario.getNombre());
        existing.setApellidos(updatedUsuario.getApellidos());
        existing.setEmail(updatedUsuario.getEmail());
        existing.setTelefono(updatedUsuario.getTelefono());
        existing.setRol(updatedUsuario.getRol());
        existing.setActivo(updatedUsuario.getActivo());
        existing.setPuntosTotal(updatedUsuario.getPuntosTotal());
        existing.setNivel(updatedUsuario.getNivel());
        if (updatedUsuario.getPassword() != null && !updatedUsuario.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedUsuario.getPassword()));
        }
        existing.setFechaActualizacion(LocalDateTime.now());
        return usuarioRepository.save(existing);
    }
}