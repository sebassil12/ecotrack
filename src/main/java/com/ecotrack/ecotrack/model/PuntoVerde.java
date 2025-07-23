package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "puntos_verdes",
       indexes = {
           @Index(name = "idx_activo", columnList = "activo"),
           @Index(name = "idx_usuario_responsable", columnList = "usuario_id"),
           @Index(name = "idx_ubicacion", columnList = "latitud, longitud")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuntoVerde {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del punto verde es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String descripcion;
    
    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "Latitud inválida")
    @DecimalMax(value = "90.0", message = "Latitud inválida")
    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitud;
    
    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "Longitud inválida")
    @DecimalMax(value = "180.0", message = "Longitud inválida")
    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitud;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(length = 200)
    private String direccion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    
    // Relaciones
    @OneToMany(mappedBy = "puntoVerde", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Recoleccion> recolecciones;
}