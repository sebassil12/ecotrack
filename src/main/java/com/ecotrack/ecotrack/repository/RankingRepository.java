package com.ecotrack.ecotrack.repository;

// RankingRepository.java
import com.ecotrack.ecotrack.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    // Puedes agregar consultas personalizadas si es necesario, ej.:
    // List<Ranking> findByAñoAndMes(Integer año, Integer mes);
}