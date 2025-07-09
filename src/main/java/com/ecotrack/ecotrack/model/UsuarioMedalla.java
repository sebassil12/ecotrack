package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuario_medalla",
       indexes = {
           @Index(name = "idx_usuario_medalla", columnList = "usuario_id, medalla_id"),
           @Index(name = "idx_fecha_obtenida", columnList = "fecha_obtenida")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioMedalla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotNull(message = "La medalla es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medalla_id", nullable = false)
    private Medalla medalla;
    
    @CreationTimestamp
    @Column(name = "fecha_obtenida", nullable = false)
    private LocalDateTime fechaObtenida;
    
    @Column(name = "puntos_momento")
    private Integer puntosMomento;
}