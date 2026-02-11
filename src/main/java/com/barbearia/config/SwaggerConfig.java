package com.barbearia.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger / OpenAPI 3.
 * Documenta todos os endpoints REST com suporte a autenticação JWT.
 * Acesse: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Sistema de Barbearia API")
                .description("API REST completa para gerenciamento de barbearia. " +
                    "Faça login em /auth/login para obter seu token JWT e clique em 'Authorize' para autenticar.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Equipe Barbearia")
                    .email("contato@barbearia.com"))
                .license(new License().name("MIT")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Insira o token JWT retornado pelo endpoint /auth/login")));
    }
}
