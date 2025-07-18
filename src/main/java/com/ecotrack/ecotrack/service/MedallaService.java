package com.ecotrack.ecotrack.service;

import java.util.List;
import java.util.Optional;

import com.ecotrack.ecotrack.model.Medalla;

public interface MedallaService {
    Medalla save(Medalla medalla);
    Optional<Medalla> findById(Long id);
    List<Medalla> findAll();
    void deleteById(Long id);
}