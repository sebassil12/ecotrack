package com.ecotrack.ecotrack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Index;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usuarios", 
       indexes = {
           @Index(name = "idx_email", columnList = "email"),
           @Index(name = "idx_rol", columnList = "rol"),
           @Index(name = "idx_puntos_total", columnList = "puntos_total")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellidos;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Column(unique = true, nullable = false, length = 150)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;
    
    @Column(name = "puntos_total", nullable = false)
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    @Builder.Default
    private Integer puntosTotal = 0;
    
    @Column(nullable = false)
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Builder.Default
    private Integer nivel = 1;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Pattern(regexp = "^(09)?[0-9\\-\\s]{8,18}$", message = "El teléfono debe ser numérico, opcionalmente empezando con '09', y con longitud válida (ej: 0912345678 o 12345678)")
    @Column(length = 20)
    private String telefono;
    
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Relaciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recoleccion> recolecciones;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PuntoVerde> puntosVerdes;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ranking> rankings;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "usuario_medalla",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "medalla_id")
    )
    private Set<Medalla> medallas;
    
    public enum RolUsuario {
        CIUDADANO, RECOLECTOR, ADMINISTRADOR
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getUsername() {
        // El email será el "username" para Spring Security
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}