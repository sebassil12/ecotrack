package com.ecotrack.ecotrack.service;

import java.util.List;

import com.ecotrack.ecotrack.model.TipoResiduo;

public interface TipoResiduoService {

    TipoResiduo createTipoResiduo(TipoResiduo tipoResiduo);

    List<TipoResiduo> getAll();

    TipoResiduo getById(Long id);

    TipoResiduo updateTipoResiduo(Long id, TipoResiduo updatedTipoResiduo);

    void deleteTipoResiduo(Long id);
}