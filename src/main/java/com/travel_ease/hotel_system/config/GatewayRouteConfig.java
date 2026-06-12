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

    @Value("${services.payment-url}")
    private String paymentServiceUrl;

    @Value("${services.user-url}")
    private String userServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("booking-service", r -> r
                        .path("/booking-service/api/bookings/**")
                        .filters(f -> f.rewritePath("/booking-service/api/bookings/(?<segment>.*)", "/booking-service/api/v1/bookings/${segment}"))
                        .uri(bookingServiceUrl)
                )
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .filters(f -> f.rewritePath("/api/payments(?<segment>/?.*)", "/api/v1/payments${segment}"))
                        .uri(paymentServiceUrl)
                )
                .route("hotel-service", r -> r
                        .path("/hotel-service/api/hotels/**", "/hotel-service/api/rooms/**")
                        .filters(f -> f.rewritePath("/hotel-service/api/(?<category>hotels|rooms)(?<segment>/?.*)", "/hotel-service/api/v1/${category}${segment}"))
                        .uri(hotelServiceUrl)
                )
                .route("user-service", r -> r
                        .path("/user-service/api/users/**")
                        .filters(f -> f.rewritePath("/user-service/api/users(?<segment>/?.*)", "/user-service/api/v1/users${segment}"))
                        .uri(userServiceUrl)
                )
                .build();
    }

}