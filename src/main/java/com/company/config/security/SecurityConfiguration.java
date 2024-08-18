package com.company.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.company.config.exception.AuthExceptionHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private AuthExceptionHandler authExceptionHandler;

	@Autowired
	private JWTAuthorizationFilter jwtAuthorizationFilter;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
		http.cors(withDefaults()).csrf((csrf) -> csrf.disable())
				.authorizeHttpRequests((requests) -> requests.requestMatchers(HttpMethod.POST, "/api/v1/auth/login")
						.anonymous().requestMatchers("/api/v1/users/**").hasAnyAuthority("ADMIN", "MANAGER")
						.anyRequest().authenticated())
				.httpBasic(withDefaults())
				.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
				.authenticationEntryPoint(authExceptionHandler).accessDeniedHandler(authExceptionHandler);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.applyPermitDefaultValues();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
