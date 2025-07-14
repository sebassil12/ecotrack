package com.ecotrack.ecotrack.repository;

import com.ecotrack.ecotrack.model.TipoResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, Long> {

    // Custom query using your index on nombre
    @Query("SELECT t FROM TipoResiduo t WHERE t.nombre LIKE %:nombre%")
    List<TipoResiduo> findByNombreContaining(String nombre);

    // Find active ones
    List<TipoResiduo> findByActivo(Boolean activo);
}