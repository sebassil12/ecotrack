package com.ecotrack.ecotrack.dto;

import java.time.LocalDateTime;

import com.ecotrack.ecotrack.model.Usuario.RolUsuario;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditUsuarioDTO {
    
    private Long id; // For binding the user ID
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    private String apellidos;
    
    @NotBlank(message = "El email es obligatorio") // Still validate format, but we'll pre-fill it
    @Email(message = "Formato de email inválido")
    @Size(max = 150)
    private String email;
    
    private String password;
    
    // @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    // @Pattern(regexp = "^(09)?[0-9\\-\\s]{8,18}$", message = "El teléfono debe ser numérico, opcionalmente empezando con '09', y con longitud válida (ej: 0912345678 o 12345678)")
    // @Column(length = 20)
    private String telefono;
    
    // Add other editable fields if needed (e.g., for admin: rol, activo, etc.)
    private RolUsuario rol;
    
    private Integer puntosTotal;
    
    private Integer nivel;
    
    private LocalDateTime fechaRegistro;
    
    private LocalDateTime fechaActualizacion;
    
    private Boolean activo;
    
}