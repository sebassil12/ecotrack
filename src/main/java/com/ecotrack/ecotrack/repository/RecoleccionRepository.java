package com.ecotrack.ecotrack.repository;

import com.ecotrack.ecotrack.model.Recoleccion;
import com.ecotrack.ecotrack.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecoleccionRepository extends JpaRepository<Recoleccion, Long> {

    // Custom query to find by user and date (using your index)
    @Query("SELECT r FROM Recoleccion r WHERE r.usuario.id = :usuarioId AND r.fechaRecoleccion >= :startDate")
    List<Recoleccion> findByUsuarioAndFecha(Long usuarioId, LocalDateTime startDate);

    // Find by tipoResiduo (using your index)
    List<Recoleccion> findByTipoResiduoId(Long tipoResiduoId);

    // Find validated ones (using your index)
    List<Recoleccion> findByValidado(Boolean validado);

	List<Recoleccion> findByUsuario(Usuario usuario);
}