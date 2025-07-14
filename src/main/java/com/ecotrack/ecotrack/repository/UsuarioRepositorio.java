package com.ecotrack.ecotrack.repository; // Adjust package as needed

import com.ecotrack.ecotrack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByEmail(String email); // For login and uniqueness check
    boolean existsByEmail(String email); // For registration validation
}