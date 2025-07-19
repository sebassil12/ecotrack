package com.ecotrack.ecotrack.service;

import java.util.List;

import com.ecotrack.ecotrack.model.Recoleccion;
import com.ecotrack.ecotrack.model.Usuario;
public interface RecoleccionService {

    Recoleccion registrarRecoleccion(Recoleccion recoleccion);

    Recoleccion getById(Long id);

    Recoleccion updateRecoleccion(Long id, Recoleccion updatedRecoleccion);

    void deleteRecoleccion(Long id);

    Recoleccion validarRecoleccion(Long id, Usuario validadoPor);

    List<Recoleccion> getAll();

    List<Recoleccion> getAllByUsuario(Usuario usuario);

    List<Recoleccion> getValidated();
}
