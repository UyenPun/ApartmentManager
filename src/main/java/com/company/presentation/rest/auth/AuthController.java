package com.company.presentation.rest.auth;

import com.company.adaptor.database.form.LoginForm;
import com.company.presentation.annotation.validation.RefreshTokenValid;
import com.company.presentation.rest.auth.response.LoginInfoDto;
import com.company.presentation.rest.auth.response.TokenDTO;
import com.company.service.IAuthService;
import com.company.service.IJWTTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
