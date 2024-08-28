package com.company.adaptor.database.form;

import com.company.domain.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserFilterForm {

    private String username;
    private String email;
    private User.Role role;

}
