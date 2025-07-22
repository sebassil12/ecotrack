package com.ecotrack.ecotrack.dto;

import java.math.BigDecimal;

public class PuntoVerdeDTO {
    private String nombre;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;

    // Constructors (manual or Lombok @Builder)
    public PuntoVerdeDTO(String nombre, String direccion, BigDecimal latitud, BigDecimal longitud) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters (no setters needed for DTO)
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public BigDecimal getLatitud() { return latitud; }
    public BigDecimal getLongitud() { return longitud; }
}
