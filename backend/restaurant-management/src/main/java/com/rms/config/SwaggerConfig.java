package com.rms.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI restaurantOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("Restaurant Management System API")
                        .version("1.0")
                        .description("REST APIs for Restaurant Management System")
                        .contact(new Contact()
                                .name("Vikas Chauke")
                                .email("vikas@example.com")))

                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://example.com"));
    }
}