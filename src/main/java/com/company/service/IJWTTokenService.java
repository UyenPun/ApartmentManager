package com.company.service;

import com.company.domain.entity.Token;
import com.company.domain.entity.User;
import com.company.presentation.rest.auth.response.TokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

// (1)
public interface IJWTTokenService {

    String generateJWTToken(String username);

    Authentication parseTokenToUserInformation(HttpServletRequest request); // đầu vào 1 token -> đầu ra user, pass của thằng đấy

    Token generateRefreshToken(User user);

    boolean isRefreshTokenValid(String refreshToken);

    TokenDTO getNewToken(String refreshToken);

}
