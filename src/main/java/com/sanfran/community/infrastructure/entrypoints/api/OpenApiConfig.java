package com.sanfran.community.infrastructure.entrypoints.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SanFran Community API")
                        .version("1.0.0")
                        .description("REST API for users, facilities and reservations built with Clean Architecture.")
                        .contact(new Contact()
                                .name("SanFran Community")
                                .email("support@sanfran.community"))
                        .license(new License()
                                .name("Internal Use")
                                .url("https://sanfran.community/internal")));
    }
}
