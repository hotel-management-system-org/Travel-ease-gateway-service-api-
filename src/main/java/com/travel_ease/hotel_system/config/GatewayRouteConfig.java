package com.travel_ease.hotel_system.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("booking-service", r -> r
                        .path("/booking-service/api/bookings/**")
                        .filters(f -> f
                                .rewritePath("/booking-service/api/bookings/(?<segment>.*)",
                                        "/booking-service/api/v1/bookings/${segment}"
                                )
                        )
                        .uri("lb://BOOKING-SERVICE-API")
                )

                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .filters(f -> f
                                .rewritePath("/api/payments(?<segment>/?.*)",
                                        "/api/v1/payments${segment}"
                                )
                        )
                        .uri("lb://PAYMENT-SERVICE-API")
                )

                .route("hotel-service", r -> r
                        .path("/hotel-service/api/hotels/**", "/hotel-service/api/rooms/**")
                        .filters(f -> f
                                .rewritePath("/hotel-service/api/(?<category>hotels|rooms)(?<segment>/?.*)",
                                        "/hotel-service/api/v1/${category}${segment}"
                                )
                        )
                        .uri("lb://HOTEL-SERVICE-API")
                )

                .route("user-service", r -> r
                        .path("/user-service/api/users/**")
                        .filters(f -> f
                                .rewritePath("/user-service/api/users(?<segment>/?.*)",
                                        "/user-service/api/v1/users${segment}"
                                )
                        )
                        .uri("lb://USER-SERVICE-API")
                )
                .build();
    }
}