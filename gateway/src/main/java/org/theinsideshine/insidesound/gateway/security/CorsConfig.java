package org.theinsideshine.insidesound.gateway.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import org.springframework.web.server.WebFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://192.168.0.137:5173/"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);

        // Configurar la fuente de configuración de CORS
        CorsConfigurationSource corsConfigurationSource = exchange -> corsConfig;

        // Crear el filtro de CORS
        WebFilter corsFilter = new CorsWebFilter(corsConfigurationSource);

        return new CorsWebFilter(corsConfigurationSource);
    }
}
