package com.ecotrack.ecotrack.service.impl;

import com.ecotrack.ecotrack.model.TipoResiduo;
import com.ecotrack.ecotrack.repository.TipoResiduoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoResiduoService {

    @Autowired
    private TipoResiduoRepository tipoResiduoRepository;

    @Transactional
    public TipoResiduo createTipoResiduo(TipoResiduo tipoResiduo) {
        return tipoResiduoRepository.save(tipoResiduo);
    }

    public List<TipoResiduo> getAll() {
        return tipoResiduoRepository.findAll();
    }

    public TipoResiduo getById(Long id) {
        return tipoResiduoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TipoResiduo not found"));
    }

    @Transactional
    public TipoResiduo updateTipoResiduo(Long id, TipoResiduo updatedTipoResiduo) {
        TipoResiduo existing = getById(id);
        // Manual merge of fields
        existing.setNombre(updatedTipoResiduo.getNombre());
        existing.setDescripcion(updatedTipoResiduo.getDescripcion());
        existing.setPuntosBase(updatedTipoResiduo.getPuntosBase());
        existing.setUnidadMedida(updatedTipoResiduo.getUnidadMedida());
        existing.setActivo(updatedTipoResiduo.getActivo());
        return tipoResiduoRepository.save(existing);
    }

    @Transactional
    public void deleteTipoResiduo(Long id) {
        // Optional: Check for related recolecciones before delete if needed
        tipoResiduoRepository.deleteById(id);
    }
}