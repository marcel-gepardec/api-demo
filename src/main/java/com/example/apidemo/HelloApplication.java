package com.example.apidemo;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@ApplicationPath("/api")
@OpenAPIDefinition(info = @Info(
        title = "API Demo",
        version = "1.0.0",
        contact = @Contact(
                name = "Api Demo",
                email = "api.demo@api-demo.com",
                url = "http://www.api-demo.com")
)
)
@SecurityScheme(
        securitySchemeName = HttpHeaders.AUTHORIZATION,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class HelloApplication extends Application {

}