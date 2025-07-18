package com.ecotrack.ecotrack.service.impl; // Ajusta el paquete

import com.ecotrack.ecotrack.model.Ranking;
import com.ecotrack.ecotrack.repository.RankingRepository;
import com.ecotrack.ecotrack.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    @Override
    public Ranking save(Ranking ranking) {
        return rankingRepository.save(ranking);
    }

    @Override
    public Optional<Ranking> findById(Long id) {
        return rankingRepository.findById(id);
    }

    @Override
    public List<Ranking> findAll() {
        return rankingRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        rankingRepository.deleteById(id);
    }
}