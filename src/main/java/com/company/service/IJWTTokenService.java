package com.company.service;

import org.springframework.security.core.Authentication;

import com.company.entity.User;
import com.company.dto.TokenDTO;
import com.company.entity.Token;

import jakarta.servlet.http.HttpServletRequest;

// (1)
public interface IJWTTokenService {

	String generateJWTToken(String username);

	Authentication parseTokenToUserInformation(HttpServletRequest request); // đầu vào 1 token -> đầu ra user, pass của thằng đấy

	Token generateRefreshToken(User user);

	boolean isRefreshTokenValid(String refreshToken);

	TokenDTO getNewToken(String refreshToken);

}
