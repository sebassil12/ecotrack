package com.ecotrack.ecotrack.configuration; // Adjust package

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                // Allow public access to login, registration, and static resources
                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**").permitAll()
                // Secure your app's endpoints (require login)
                .requestMatchers("/dashboard/**", "/puntos-verdes/**", "/api/**").authenticated()
                .requestMatchers("/admin/usuarios/access-denied").permitAll()
                // Example: Role-based (uncomment if needed for admin-only pages)
                .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
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
                .accessDeniedPage("/admin/usuarios/access-denied")  // ¡Esto redirige a tu endpoint en caso de 403!
            );

        return http.build();
    }
}