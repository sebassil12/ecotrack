package com.ecotrack.ecotrack.service.impl;

// MedallaServiceImpl.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecotrack.ecotrack.model.Medalla;
import com.ecotrack.ecotrack.repository.MedallaRepository;
import com.ecotrack.ecotrack.service.MedallaService;

import java.util.List;
import java.util.Optional;

@Service
public class MedallaServiceImpl implements MedallaService {

    @Autowired
    private MedallaRepository medallaRepository;

    @Override
    public Medalla save(Medalla medalla) {
        return medallaRepository.save(medalla);
    }

    @Override
    public Optional<Medalla> findById(Long id) {
        return medallaRepository.findById(id);
    }

    @Override
    public List<Medalla> findAll() {
        return medallaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        medallaRepository.deleteById(id);
    }
}
