package com.ecotrack.ecotrack.configuration; // Adjust package

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService usuarioDetailsService;

    public SecurityConfig(UserDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // For hashing passwords
    }

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permitido para todo el publico
                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**").permitAll()
                .requestMatchers("/access-denied").permitAll()

                // Autenticación requerida para las siguientes rutas
                .requestMatchers("/dashboard/**", "/puntos-verdes/**").authenticated()

                // Rutas protegidas por roles
                .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
                
                //Tipos de residuos
                .requestMatchers(HttpMethod.POST, "/tipos-residuo").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/tipos-residuo/edit/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/tipos-residuo/update/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/tipos-residuo/delete/**").hasAuthority("ADMINISTRADOR")

                // Medallas
                .requestMatchers(HttpMethod.POST, "/medallas").hasAuthority("ADMINISTRADOR")
                .requestMatchers( "/medallas/new").hasAuthority("ADMINISTRADOR")
                // .requestMatchers(HttpMethod.POST, "/tipos-residuo/delete/**").hasAuthority("ADMINISTRADOR")

                // Puntos verdes
                .requestMatchers( "/api/puntos-verdes/registrar").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/puntos-verdes/registrar").hasAuthority("ADMINISTRADOR")
                .requestMatchers( "/api/puntos-verdes/editar/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/puntos-verdes/editar/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/puntos-verdes/eliminar/**").hasAuthority("ADMINISTRADOR")

                .anyRequest().authenticated() // All other requests require auth
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/dashboard", true) // Redirect after login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Redirect after logout
                .permitAll()
            )
            .userDetailsService(usuarioDetailsService) // Use your custom service

            .exceptionHandling((exceptions) -> exceptions
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                // Log para debug (opcional, quítalo si no lo necesitas)
                System.err.println("Access Denied for request: " + request.getMethod() + " " + request.getRequestURI() + " - " + accessDeniedException.getMessage());
                // Redirige a tu página de error
                response.sendRedirect("/access-denied");
            })
            );

        return http.build();
    }
}