package com.ecotrack.ecotrack.service;

import java.util.List;

import com.ecotrack.ecotrack.model.Recoleccion;
import com.ecotrack.ecotrack.model.Usuario;
public interface RecoleccionService {

    public Recoleccion registrarRecoleccion(Recoleccion recoleccion);

    public Recoleccion getById(Long id);

    public Recoleccion updateRecoleccion(Long id, Recoleccion updatedRecoleccion);

    public void deleteRecoleccion(Long id);

    public Recoleccion validarRecoleccion(Long id, Usuario validadoPor);

    public List<Recoleccion> getAll();

    public List<Recoleccion> getByUsuario(Long usuarioId);

    public List<Recoleccion> getValidated();
}
