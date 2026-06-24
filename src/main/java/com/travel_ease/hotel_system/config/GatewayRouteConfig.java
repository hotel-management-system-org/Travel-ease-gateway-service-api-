package com.travel_ease.hotel_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Value("${services.booking-url}")
    private String bookingServiceUrl;

    @Value("${services.hotel-url}")
    private String hotelServiceUrl;

    @Value("${services.user-url}")
    private String userServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("booking-service", r -> r
                        .path("/api/bookings/**")
                        .filters(f -> f.rewritePath("/api/bookings/(?<segment>.*)", "/api/v1/bookings/${segment}"))
                        .uri(bookingServiceUrl)
                )
                .route("hotel-service", r -> r
                        .path("/api/hotels/**", "/api/rooms/**")
                        .filters(f -> f.rewritePath("/api/(?<category>hotels|rooms)(?<segment>/?.*)", "/api/v1/hotels/${category}${segment}"))
                        .uri(hotelServiceUrl)
                )
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.rewritePath("/api/users(?<segment>/?.*)", "/api/v1/users${segment}"))
                        .uri(userServiceUrl)
                )
                .build();
    }

}