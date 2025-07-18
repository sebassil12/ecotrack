package com.ecotrack.ecotrack.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.ecotrack.ecotrack.dto.PuntoVerdeRequest;
import com.ecotrack.ecotrack.dto.PuntoVerdeResponse;
import com.ecotrack.ecotrack.model.PuntoVerde;
import com.ecotrack.ecotrack.repository.PuntoVerdeRepositorio;
import com.ecotrack.ecotrack.service.PuntoVerdeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuntoVerdeServiceImpl implements PuntoVerdeService {
	
	private final PuntoVerdeRepositorio repository;
    private final WebClient nominatimClient;

    public PuntoVerdeResponse registrar(PuntoVerdeRequest request) {
        String direccion = obtenerDireccion(request.latitud(), request.longitud());
        PuntoVerde punto = PuntoVerde.builder()
            .nombre(request.nombre())
            .descripcion(request.descripcion())
            .latitud(request.latitud())
            .longitud(request.longitud())
            .direccion(direccion != null && direccion.length() > 200 ? direccion.substring(0, 200) : direccion)
            .activo(true)
            .build();
        repository.save(punto);
        
        return new PuntoVerdeResponse(
            punto.getId(),
            punto.getNombre(),
            punto.getDescripcion(),
            punto.getLatitud(),
            punto.getLongitud(),
            punto.getDireccion()
        );
    }

    private String obtenerDireccion(BigDecimal lat, BigDecimal lon) {
        String url = UriComponentsBuilder.fromPath("/reverse")
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("format", "json")
            .toUriString();

        NominatimResponse response = nominatimClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(NominatimResponse.class)
            .block();

        if (response == null || response.display_name() == null) {
            throw new RuntimeException("No se pudo obtener la dirección de Nominatim");
        }
        return response.display_name();
    }

    // DTO interno para la respuesta de Nominatim
    public record NominatimResponse(String display_name) {}
    
    public PuntoVerde buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("PuntoVerde no encontrado"));
    }
    
    public List<PuntoVerde> listarTodos() {
        return repository.findAll();
    }

	@Override
	public void eliminarPunto(Long id) {
		if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("PuntoVerde no encontrado con ID: " + id);
        }
		
	}
}
