package com.proyectogimnasio.cliente.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clienteOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API Gimnasio")
                        .description("Microservicio de Clientes")
                        .version("1.0"));
    }
}
