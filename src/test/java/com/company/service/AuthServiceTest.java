package com.company.service;

import com.company.domain.entity.Token;
import com.company.domain.entity.User;
import com.company.presentation.rest.auth.response.LoginInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private IJWTTokenService jwtTokenService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // Arrange
        String username = "testuser";
        User mockUser = new User(); // Create a mock User object
        mockUser.setUsername(username);

        LoginInfoDto mockDto = new LoginInfoDto();
        mockDto.setUsername(username);

        String jwtToken = "jwt-token";
        String refreshTokenKey = "refresh-token-key";

        Token refreshToken = new Token();
        refreshToken.setKey(refreshTokenKey);

        when(userService.getUserByUsername(username)).thenReturn(mockUser);
        when(modelMapper.map(mockUser, LoginInfoDto.class)).thenReturn(mockDto);
        when(jwtTokenService.generateJWTToken(username)).thenReturn(jwtToken);
        when(jwtTokenService.generateRefreshToken(mockUser)).thenReturn(refreshToken);

        // Act
        LoginInfoDto result = authService.login(username);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals(jwtToken, result.getToken());
        assertEquals(refreshTokenKey, result.getRefreshToken());

        verify(userService, times(1)).getUserByUsername(username);
        verify(modelMapper, times(1)).map(mockUser, LoginInfoDto.class);
        verify(jwtTokenService, times(1)).generateJWTToken(username);
        verify(jwtTokenService, times(1)).generateRefreshToken(mockUser);
    }
}
