package com.ourMenu.backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "ourmenu API 명세서",
                description = "ourmenu 사용되는 API 명세서",
                version = "v1"
        )/*,
        servers = {
                @Server(
                        url = "https://bluesparrow.shop",
                        description = "HTTPS Server"
                ),
        }*/
)

@Configuration
public class SwaggerConfig {

}