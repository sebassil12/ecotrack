package com.ecotrack.ecotrack.service;

import java.util.List;

import com.ecotrack.ecotrack.model.TipoResiduo;

public interface TipoResiduoService {

    public TipoResiduo createTipoResiduo(TipoResiduo tipoResiduo);

    public List<TipoResiduo> getAll();

    public TipoResiduo getById(Long id);

    public TipoResiduo updateTipoResiduo(Long id, TipoResiduo updatedTipoResiduo);

    public void deleteTipoResiduo(Long id);
}