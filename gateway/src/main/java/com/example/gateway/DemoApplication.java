package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoApplication {

    public static void main(String... args) {

        SpringApplication.run(DemoApplication.class, args);

    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_service", r -> r.path("/users/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                            if (authHeader != null) {
                                exchange = exchange.mutate().request(
                                        exchange.getRequest().mutate()
                                                .header(HttpHeaders.AUTHORIZATION, authHeader)
                                                .build())
                                        .build();
                            }
                            return chain.filter(exchange);
                        }))
                        .uri("http://localhost:9091"))

                .route("order_service", r -> r.path("/orders/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                            if (authHeader != null) {
                                exchange = exchange.mutate().request(
                                        exchange.getRequest().mutate()
                                                .header(HttpHeaders.AUTHORIZATION, authHeader)
                                                .build())
                                        .build();
                            }
                            return chain.filter(exchange);
                        }))
                        .uri("http://localhost:9106"))
                .build();
    }

}
