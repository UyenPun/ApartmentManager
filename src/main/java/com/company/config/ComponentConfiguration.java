package com.company.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class ComponentConfiguration {

	@Bean
	public ModelMapper initModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
		return pageableResolver -> {
			pageableResolver.setOneIndexedParameters(true);
		};
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/**")
						.allowedOrigins("http://127.0.0.1:5501")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*");
			}
		};
	}
	
	
	/**
	 * UI: http://localhost:8080/swagger-ui/index.html
	 */
//	Swagger: Bảng giới thiệu về BE
	@Bean
	  public OpenAPI myOpenAPI() {
			Contact contact = new Contact()
					.email("tranuyen2018@gmail.com")
					.name("Tran Uyen")
					.url("https://www.facebook.com/punpun2812/");

			License license = new License()
					.name("Apache 2.0")
					.url("http://www.apache.org/licenses/LICENSE-2.0.html");

			Info info = new Info()
					.title("ApartmentManager Application API")
					.version("1.0")
					.contact(contact)
					.description("This is API description for Application")
					.termsOfService("Terms of service URL")
					.license(license);

	    return new OpenAPI().info(info);
	  }

}
