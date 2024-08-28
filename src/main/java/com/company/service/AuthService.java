package com.company.service;

import com.company.domain.entity.Token;
import com.company.domain.entity.User;
import com.company.presentation.rest.auth.response.LoginInfoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
