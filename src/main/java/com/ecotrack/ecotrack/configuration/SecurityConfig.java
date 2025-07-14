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
        	    // ... existing public matchers like /login, /registro ...
        	    .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR") // Only admins access /admin/**
        	    .requestMatchers("/perfil/**").authenticated() // Any authenticated user for self-profile
        	    .requestMatchers("/dashboard/**", "/puntos-verdes/**", "/api/**").authenticated()
        	    .anyRequest().authenticated()
        	)
        	// ... rest of the config unchanged
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
            .userDetailsService(usuarioDetailsService); // Use your custom service

        return http.build();
    }
}