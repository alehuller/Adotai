package br.com.alevhvm.adotai.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

        @Bean
        OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Documentação API do sistema de adoções")
                                                .version("v1")
                                                .description("Procedimentos CRUD do sistema de adoções.")
                                                .termsOfService("Link dos termos de serviço.")
                                                .license(
                                                                new License()
                                                                                .name("Apache 2.0")
                                                                                .url("URL")));

        }
        // http://localhost:8080/swagger-ui/index.html
        // http://localhost:8080/v3/api-docs
}