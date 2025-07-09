package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "logs_actividad",
       indexes = {
           @Index(name = "idx_fecha_log", columnList = "fecha_log"),
           @Index(name = "idx_usuario_log", columnList = "usuario_id"),
           @Index(name = "idx_accion", columnList = "accion"),
           @Index(name = "idx_nivel", columnList = "nivel")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogActividad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @NotBlank(message = "La acción es obligatoria")
    @Size(min = 2, max = 100, message = "La acción debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String accion;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;
    
    @NotNull(message = "El nivel es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelLog nivel;
    
    @Size(max = 45, message = "La IP no puede exceder 45 caracteres")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Size(max = 255, message = "El user agent no puede exceder 255 caracteres")
    @Column(name = "user_agent", length = 255)
    private String userAgent;
    
    @CreationTimestamp
    @Column(name = "fecha_log", nullable = false)
    private LocalDateTime fechaLog;
    
    @Size(max = 50, message = "El módulo no puede exceder 50 caracteres")
    @Column(length = 50)
    private String modulo;
    
    public enum NivelLog {
        INFO, WARNING, ERROR, DEBUG
    }
}