package com.company.presentation.annotation.validator;

import com.company.presentation.annotation.validation.RefreshTokenValid;
import com.company.service.IJWTTokenService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

// Validation annotation
public class RefreshTokenValidValidator implements ConstraintValidator<RefreshTokenValid, String> {

    @Autowired
    private IJWTTokenService jwtTokenService;

    @SuppressWarnings("deprecation")
    @Override
    public boolean isValid(String refreshToken, ConstraintValidatorContext constraintValidatorContext) {

        if (StringUtils.isEmpty(refreshToken)) {
            return false;
        }

        return jwtTokenService.isRefreshTokenValid(refreshToken);
    }
}
