package com.company.presentation.annotation.validation;

// Validation annotation

import com.company.presentation.annotation.validation.RefreshTokenValid.List;
import com.company.presentation.annotation.validator.RefreshTokenValidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {RefreshTokenValidValidator.class})
@Repeatable(List.class)
public @interface RefreshTokenValid { // Tạo annonation để check xem fresher token user nhập vào hợp lệ không
    String message() default "RefreshToken is not valid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        RefreshTokenValid[] value();
    }
}
