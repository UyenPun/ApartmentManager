package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.LoginInfoDto;
import com.company.dto.TokenDTO;
import com.company.form.LoginForm;
import com.company.service.IAuthService;
import com.company.service.IJWTTokenService;
import com.company.validation.auth.RefreshTokenValid;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/auth")
@Validated
public class AuthController {

	@Autowired
	private IAuthService authService;
	
	@Autowired
	private IJWTTokenService jwtTokenService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public LoginInfoDto login(@RequestBody @Valid LoginForm loginForm) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginForm.getUsername(), 
						loginForm.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return authService.login(loginForm.getUsername());
	}
	
	@GetMapping("/refreshtoken")
	public TokenDTO refreshtoken(@RefreshTokenValid String refreshToken) {
		return jwtTokenService.getNewToken(refreshToken);
	}
}
