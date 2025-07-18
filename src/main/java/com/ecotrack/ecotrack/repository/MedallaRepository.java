package com.ecotrack.ecotrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecotrack.ecotrack.model.Medalla;

@Repository
public interface MedallaRepository extends JpaRepository<Medalla, Long> {
    // Puedes agregar consultas personalizadas si es necesario, por ejemplo:
    // List<Medalla> findByTipo(Medalla.TipoMedalla tipo);
}