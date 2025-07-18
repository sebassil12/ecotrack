
// RankingService.java
package com.ecotrack.ecotrack.service; // Ajusta el paquete

import com.ecotrack.ecotrack.model.Ranking;

import java.util.List;
import java.util.Optional;

public interface RankingService {
    Ranking save(Ranking ranking);
    Optional<Ranking> findById(Long id);
    List<Ranking> findAll();
    void deleteById(Long id);
}