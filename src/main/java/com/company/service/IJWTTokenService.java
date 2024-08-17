package com.company.service;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
// (1)
public interface IJWTTokenService {

	String generateJWTToken(String username);
	
	Authentication parseTokenToUserInformation(HttpServletRequest request); // đầu vào 1 token -> đầu ra user, pass của thằng đấy
	
}
