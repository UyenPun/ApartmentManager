package com.company.presentation.annotation.validator;

import com.company.presentation.annotation.validation.UserNameNotExists;
import com.company.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UserNameNotExistsValidator implements ConstraintValidator<UserNameNotExists, String> {

    @Autowired
    private IUserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // Nếu tên người dùng trống hoặc null, coi là hợp lệ (hoặc bạn có thể chỉnh sửa
        // tùy theo yêu cầu)
        if (StringUtils.isEmpty(username)) {
            return true;
        }

        // Kiểm tra sự tồn tại của User bằng tên người dùng
        return !userService.isUserExistsByUsername(username);
    }
}
