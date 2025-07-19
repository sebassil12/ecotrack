package com.ecotrack.ecotrack.service;

import java.util.List;

import com.ecotrack.ecotrack.dto.PuntoVerdeRequest;
import com.ecotrack.ecotrack.dto.PuntoVerdeResponse;
import com.ecotrack.ecotrack.model.PuntoVerde;

public interface PuntoVerdeService {
	
	List<PuntoVerde> listarTodos();

	PuntoVerdeResponse registrar(PuntoVerdeRequest request);

	PuntoVerde buscarPorId(Long id);

	void eliminarPunto(Long id);

	PuntoVerdeResponse actualizar(Long id, PuntoVerdeRequest request);
}
