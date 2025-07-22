package com.ecotrack.ecotrack.service.impl; // Adjust package

import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.repository.UsuarioRepositorio;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepository;

    public UsuarioDetailsService(UsuarioRepositorio usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Wrap in Spring's User class with authorities
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),  // Username
                usuario.getPassword(),  // Hashed password
                usuario.getActivo(),  // Enabled (assuming you have an 'activo' field)
                true,  // Account non-expired
                true,  // Credentials non-expired
                true,  // Account non-locked
                Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().name())));  // Authorities from rol enum
    }
}