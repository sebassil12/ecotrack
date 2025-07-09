package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "rankings",
       indexes = {
           @Index(name = "idx_año_mes", columnList = "año, mes"),
           @Index(name = "idx_posicion", columnList = "posicion"),
           @Index(name = "idx_usuario_periodo", columnList = "usuario_id, año, mes")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotNull(message = "El año es obligatorio")
    @Min(value = 2023, message = "Año inválido")
    @Max(value = 2030, message = "Año inválido")
    @Column(nullable = false)
    private Integer año;
    
    @NotNull(message = "El mes es obligatorio")
    @Min(value = 1, message = "Mes inválido")
    @Max(value = 12, message = "Mes inválido")
    @Column(nullable = false)
    private Integer mes;
    
    @Column(nullable = false)
    @Min(value = 1, message = "La posición debe ser positiva")
    private Integer posicion;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    @Builder.Default
    private Integer puntos = 0;
    
    @Column(name = "total_residuos", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "El total de residuos no puede ser negativo")
    @Builder.Default
    private BigDecimal totalResiduos = BigDecimal.ZERO;
    
    @CreationTimestamp
    @Column(name = "fecha_calculo", nullable = false)
    private LocalDateTime fechaCalculo;
    
    @Column(name = "recolecciones_count", nullable = false)
    @Min(value = 0, message = "El conteo de recolecciones no puede ser negativo")
    @Builder.Default
    private Integer recoleccionesCount = 0;
}