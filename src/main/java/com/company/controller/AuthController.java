package com.company.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.LoginInfoDto;
import com.company.entity.User;
import com.company.form.LoginForm;
import com.company.service.IUserService;
import com.company.service.IJWTTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/auth")
@Validated
public class AuthController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IJWTTokenService jwtTokenService;

	@Autowired
	private IUserService service;

	@PostMapping("/login")
	public LoginInfoDto login(@RequestBody @Valid LoginForm loginForm) {

		// Người dùng truyền xuống
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())); // truyền
																											// vào ->
																											// //Check
																											// dưới
																											// database

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// get entity
		User entity = service.getUserByUsername(loginForm.getUsername());

		// convert entity to dto
		LoginInfoDto dto = modelMapper.map(entity, LoginInfoDto.class);

		// add jwt token to dto
		dto.setToken(jwtTokenService.generateJWTToken(entity.getUsername()));

		return dto;
	}
}
