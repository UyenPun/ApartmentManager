package com.company.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.dto.LoginInfoDto;
import com.company.entity.User;
import com.company.entity.Token;

@Service
public class AuthService implements IAuthService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IJWTTokenService jwtTokenService;

	@Autowired
	private IUserService userService;

	@Override
	public LoginInfoDto login(String username) {
		// get entity
		User entity = userService.getUserByUsername(username);

		// convert entity to dto
		LoginInfoDto dto = modelMapper.map(entity, LoginInfoDto.class);

		// add jwt token to dto
		dto.setToken(jwtTokenService.generateJWTToken(entity.getUsername()));

		// add refresh token to dto
		Token token = jwtTokenService.generateRefreshToken(entity);
		dto.setRefreshToken(token.getKey());

		return dto;
	}
}
