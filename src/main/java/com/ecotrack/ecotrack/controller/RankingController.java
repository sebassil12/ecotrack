// RankingController.java
package com.ecotrack.ecotrack.controller; // Ajusta el paquete

import com.ecotrack.ecotrack.model.Usuario;
import com.ecotrack.ecotrack.service.impl.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rankings")
public class RankingController {

    @Autowired
    private UsuarioServiceImpl usuarioService; // Inyecta el servicio de usuarios para listar

    // Listar usuarios ordenados por puntosTotal descendente, con posiciones asignadas dinámicamente
    @GetMapping
    public String listRankings(Model model) {
        // Obtener todos los usuarios ordenados por puntosTotal descendente
        List<Usuario> usuarios = usuarioService.findAllSortedByPuntosDesc();
        
        // Asignar posiciones basadas en el orden (1 para el de más puntos, etc.)
        // Nota: Esto es dinámico y no se guarda en BD; maneja la lista en memoria
        for (int i = 0; i < usuarios.size(); i++) {
            // Puedes agregar un atributo temporal o usar en la vista; aquí asumimos que no modificamos la entidad
            // Si necesitas un DTO, crea uno para incluir posición
        }
        
        model.addAttribute("usuarios", usuarios); // Pasa la lista de usuarios con puntos y posiciones implícitas (usa índice en Thymeleaf para posición)
        return "rankings-list"; // Retorna la vista (ajusta el template para iterar sobre usuarios y mostrar posición como ${stat.index + 1})
    }
}