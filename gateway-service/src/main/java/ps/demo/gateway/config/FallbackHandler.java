package ps.demo.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FallbackHandler {

    public Mono<ServerResponse> orderFallback() {
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                            "status": "SERVICE_UNAVAILABLE",
                            "message": "Order service is currently unavailable. Please try again later."
                        }
                        """));
    }
}
