package dev.saadm.shopfloor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ShopFloor API",
                version = "1.0.0",
                description = "MES / OEE backend for manufacturing operations. "
                        + "Authenticate via POST /api/auth/login (demo users: manager / qc / operator, password 'password'), "
                        + "then click Authorize and paste the token.",
                contact = @Contact(name = "Muhammad Saad", url = "https://saadm.dev", email = "saad@saadm.dev")),
        security = @SecurityRequirement(name = "bearer-jwt"))
@SecurityScheme(
        name = "bearer-jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class OpenApiConfig {
}
