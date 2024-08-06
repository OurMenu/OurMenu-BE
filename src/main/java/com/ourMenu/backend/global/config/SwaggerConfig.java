package com.ourMenu.backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
        @Value("${SPRING_SWAGGER_SERVER_URL:http://localhost:8080}")
        private String serverUrl;

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .servers(Collections.singletonList(new Server().url(serverUrl).description("API Server")))
                        .info(new Info().title("ourmenu API 명세서").description("ourmenu 사용되는 API 명세서").version("v1"))
                        .components(new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        )
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

        }
}