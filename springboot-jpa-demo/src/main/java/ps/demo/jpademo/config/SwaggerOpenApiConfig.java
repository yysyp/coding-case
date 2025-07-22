package ps.demo.jpademo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerOpenApiConfig {


    private static final String AUTHORIZATION_TOKEN = "Authorization Token";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(AUTHORIZATION_TOKEN, apiKeySecuritySchema()))
                .info(new Info().title("The APIs").description("Restful services OpenAPI 3 documents"))
                .security(Collections.singletonList(new SecurityRequirement().addList(AUTHORIZATION_TOKEN)));

    }

    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("Authorization")
                .description("This is the JWT auth token")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
    }

}
