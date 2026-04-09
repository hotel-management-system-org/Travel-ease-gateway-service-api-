package com.travel_ease.hotel_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@EnableWebFluxSecurity
@Configuration
@EnableMethodSecurity
public class GatewaySecurityConfig {

    @Value("${keycloak.auth-issure-uri}")
    private String authIssueUri;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                    .pathMatchers("/user-service/api/users/visitors/**").permitAll()
                    .pathMatchers("/hotel-service/api/hotels/visitors/**").permitAll()
                    .pathMatchers("/hotel-service/api/hotels/policy/visitors/**").permitAll()
                    .pathMatchers("/hotel-service/api/rooms/visitor/**").permitAll()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(authIssueUri);
    }


    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(List.of("http://localhost:4200"));

        corsConfig.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }


}
