package org.theinsideshine.insidesound.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.theinsideshine.insidesound.gateway.security.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthorizationFilter authorizationFilter;

    @Autowired
    private CorsWebFilter corsWebFilter;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/msvc-security/login").permitAll()
                .pathMatchers(HttpMethod.GET, "/msvc-security/users","/msvc-security/users/page/{page}").permitAll()
                .pathMatchers(HttpMethod.GET, "/msvc-security/users/{id}").hasAnyRole("USER", "ADMIN")
                .pathMatchers(HttpMethod.POST, "/msvc-security/users").permitAll()
                .pathMatchers("/msvc-security/users/**").hasRole("ADMIN")
                .pathMatchers( HttpMethod.GET,"/msvc-albums/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(authorizationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(corsWebFilter, SecurityWebFiltersOrder.CORS)
                .csrf().disable()
                .build();
    }



}
