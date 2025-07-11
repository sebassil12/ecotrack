package com.ecotrack.ecotrack.dto;

import java.math.BigDecimal;

public record PuntoVerdeResponse(
Long id,
String nombre,
String descripcion,
BigDecimal latitud,
BigDecimal longitud,
String direccion
) {}
