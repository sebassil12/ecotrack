package com.ecotrack.ecotrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecotrack.ecotrack.model.Medalla;
import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.model.UsuarioMedalla;

public interface UsuarioMedallaRepository extends JpaRepository<UsuarioMedalla, Long> {
    // Puedes agregar consultas personalizadas si es necesario, por ejemplo:
    List<UsuarioMedalla> findByUsuario(Usuario usuario);
    // List<UsuarioMedalla> findByMedalla(Medalla medalla);
    @Query("SELECT CASE WHEN COUNT(um) > 0 THEN true ELSE false END " +
           "FROM UsuarioMedalla um " +
           "WHERE um.usuario = :usuario AND um.medalla = :medalla")
    boolean existsByUsuarioAndMedalla(@Param("usuario") Usuario usuario, @Param("medalla") Medalla medalla);
}
