package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tipos_residuo",
       indexes = {
           @Index(name = "idx_nombre_tipo", columnList = "nombre")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoResiduo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del tipo de residuo es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String descripcion;
    
    @Column(name = "puntos_base", nullable = false)
    @Min(value = 1, message = "Los puntos base deben ser positivos")
    @Builder.Default
    private Integer puntosBase = 1;
    
    @Column(name = "unidad_medida", nullable = false, length = 10)
    @NotBlank(message = "La unidad de medida es obligatoria")
    @Builder.Default
    private String unidadMedida = "kg";
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    // Relaciones
    @OneToMany(mappedBy = "tipoResiduo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recoleccion> recolecciones;
}