package com.theinsideshine.insidesound.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${openapi.version}")
    private String openApiVersion;
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Insidesound")
                        .description("Creacion y Reproduccion de albumes de musica")
                        .version(openApiVersion));
    }
}
