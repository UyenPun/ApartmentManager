package com.company.service;

import com.company.adaptor.database.form.CreatingUserForm;
import com.company.adaptor.database.form.UserFilterForm;
import com.company.domain.entity.User;
import com.company.presentation.rest.user.response.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    Page<UserDTO> getAllUsers(Pageable pageable, UserFilterForm filterForm);

    User getUserByID(int id);

    void createUser(CreatingUserForm form);

    boolean isUserExistsByUsername(String username);

    boolean isUserExistsByID(int id);

    User getUserByUsername(String username);
}
