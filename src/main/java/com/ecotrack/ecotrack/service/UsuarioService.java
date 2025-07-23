package com.ecotrack.ecotrack.service;

import java.util.List;
import java.util.Optional;

import com.ecotrack.ecotrack.dto.EditUsuarioDTO;
import com.ecotrack.ecotrack.model.Usuario;

public interface UsuarioService {

    // Create (Registration)
    Usuario registrar(Usuario usuario);

// Read All
    List<Usuario> listarTodos();

// Read by ID
    Usuario encontrarPorId(Long id);

// Read by Email
    Optional<Usuario> encontrarPorEmail(String email);
// Update
    Usuario actualizar(Usuario usuario);

// Delete
    void eliminar(Long id);

    Usuario getCurrentUser();

// Update self-profile (limited fields, no role/activo changes)
    Usuario actualizarPerfil(EditUsuarioDTO dto);

// Update any user (for admins, allows all fields including role/activo)
    Usuario actualizarUsuarioAdmin(EditUsuarioDTO updatedUsuario);
}
