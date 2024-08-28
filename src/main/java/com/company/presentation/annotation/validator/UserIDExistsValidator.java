package com.company.presentation.annotation.validator;

import com.company.presentation.annotation.validation.UserIDExists;
import com.company.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIDExistsValidator implements ConstraintValidator<UserIDExists, Integer> {

    @Autowired
    private IUserService userService;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        // Nếu ID trống hoặc null, coi là hợp lệ (hoặc bạn có thể chỉnh sửa tùy theo yêu cầu)
        if (id == null) {
            return true;
        }

        // Kiểm tra sự tồn tại của User bằng ID
        return userService.isUserExistsByID(id);
    }
}
