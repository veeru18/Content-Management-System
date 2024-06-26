package com.example.cms.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

//URL-> http://localhost:8080/swagger-ui.html
@Configuration
@OpenAPIDefinition
public class ApplicationDocumentation {

	@Bean
	Contact contact() {
		return new Contact().name("Veeresh T A")
				.url("gmail.com")
				.email("veereshta@gmail.com");
	}
	
	@Bean
	Info info() {
		return new Info().title("Content Management System")
				.description("RESTful API with  with basic CRUD operations")
				.version("v0.0.1")
				.contact(contact());
	}
	
	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(info());
	}
}