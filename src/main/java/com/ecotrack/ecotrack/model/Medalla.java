package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Set;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "medallas",
       indexes = {
           @Index(name = "idx_tipo_medalla", columnList = "tipo"),
           @Index(name = "idx_puntos_requeridos", columnList = "puntos_requeridos")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medalla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la medalla es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String descripcion;
    
    @Size(max = 100, message = "El icono no puede exceder 100 caracteres")
    @Column(length = 100)
    private String icono;
    
    @Column(name = "puntos_requeridos", nullable = false)
    @Min(value = 1, message = "Los puntos requeridos deben ser positivos")
    private Integer puntosRequeridos;
    
    @NotNull(message = "El tipo de medalla es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMedalla tipo;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;
    
    @Size(max = 50, message = "El color no puede exceder 50 caracteres")
    @Column(length = 50)
    private String color;
    
    // Relaciones
    @ManyToMany(mappedBy = "medallas", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios;
    
    public enum TipoMedalla {
        BRONCE, PLATA, ORO, PLATINO, ESPECIAL
    }
}