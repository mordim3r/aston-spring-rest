package com.example.astonspringrest;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("User API")
                        .description("REST API для управления пользователями")
                        .version("1.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")));
    }
}