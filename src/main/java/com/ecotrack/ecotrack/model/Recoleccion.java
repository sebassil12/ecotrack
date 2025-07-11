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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "recolecciones",
       indexes = {
           @Index(name = "idx_usuario_fecha", columnList = "usuario_id, fecha_recoleccion"),
           @Index(name = "idx_tipo_residuo", columnList = "tipo_residuo_id"),
           @Index(name = "idx_fecha_recoleccion", columnList = "fecha_recoleccion"),
           @Index(name = "idx_validado", columnList = "validado")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recoleccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotNull(message = "El tipo de residuo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_residuo_id", nullable = false)
    private TipoResiduo tipoResiduo;
    
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;
    
    @Column(nullable = false)
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    @Builder.Default
    private Integer puntos = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean validado = false;
    
    @CreationTimestamp
    @Column(name = "fecha_recoleccion", nullable = false)
    private LocalDateTime fechaRecoleccion;
    
    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por")
    private Usuario validadoPor;
    
    @Size(max = 255, message = "Las observaciones no pueden exceder 255 caracteres")
    @Column(length = 255)
    private String observaciones;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punto_verde_id")
    private PuntoVerde puntoVerde;
    
    @PrePersist
    public void calcularPuntos() {
        if (this.tipoResiduo != null && this.cantidad != null) {
            this.puntos = this.cantidad.multiply(BigDecimal.valueOf(this.tipoResiduo.getPuntosBase())).intValue();
        }
    }
}