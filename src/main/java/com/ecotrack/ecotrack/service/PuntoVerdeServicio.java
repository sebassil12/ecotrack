package com.ecotrack.ecotrack.service;

import java.util.List;

import com.ecotrack.ecotrack.dto.PuntoVerdeRequest;
import com.ecotrack.ecotrack.dto.PuntoVerdeResponse;
import com.ecotrack.ecotrack.model.PuntoVerde;

public interface PuntoVerdeServicio {
	
	public List<PuntoVerde> listarTodos();
	
	public PuntoVerdeResponse registrar(PuntoVerdeRequest request);
	
	public PuntoVerde buscarPorId(Long id);
	
	public void eliminarPunto(Long id);
}
