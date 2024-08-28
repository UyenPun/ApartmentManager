package com.company;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "API", version = "1.0", description = "API information")
)
public class ApartmentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApartmentManagementApplication.class, args);
    }

}
