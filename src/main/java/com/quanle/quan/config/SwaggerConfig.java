package com.quanle.quan.config;

import com.quanle.quan.models.entity.SwaggerConstant;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Product",
                        email = "abc@gmail.com",
                        url = "http://google.com"
                ),
                description = "OpenApi documentation",
                title = "OpenAPI specification",
                version = "1.0"
        ),
        servers = {@Server(
                description = "My Local",
                url = "http://localhost:8080"
        )},
        security = @SecurityRequirement(
                name = SwaggerConstant.NAME
        )
)

@SecurityScheme(
        name = SwaggerConstant.NAME,
        description = "JWT token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
