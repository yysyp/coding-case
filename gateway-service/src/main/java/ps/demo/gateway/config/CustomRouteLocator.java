package ps.demo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRouteLocator {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("custom-route", r -> r.path("/custom/**")
                        .filters(f -> f.addRequestHeader("X-Custom-Route", "custom-route")
                                .rewritePath("/custom/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8083"))
                .build();
    }
}
