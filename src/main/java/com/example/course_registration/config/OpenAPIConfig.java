package com.example.course_registration.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Course Registration System API",
        version = "1.0.0",
        description = "Complete CRUD API for Student Management in Course Registration System",
        contact = @Contact(
            name = "Development Team",
            email = "dev@courseregistration.com",
            url = "https://courseregistration.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Development Server"
        ),
        @Server(
            url = "https://api.courseregistration.com",
            description = "Production Server"
        )
    }
)
// @SecurityScheme(
//     name = "Bearer Authentication",
//     type = SecuritySchemeType.HTTP,
//     scheme = "bearer",
//     bearerFormat = "JWT",
//     description = "JWT token for API authentication",
//     in = SecuritySchemeIn.HEADER
// )
public class OpenAPIConfig {
}
