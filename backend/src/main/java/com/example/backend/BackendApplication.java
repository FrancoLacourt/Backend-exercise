package com.example.backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("lacourt-e2d595 API")
                        .version("1.0.0")
                        .description("API for Backend application")
                        .termsOfService("https://swagger.io/terms")
                        .license(new License().name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}