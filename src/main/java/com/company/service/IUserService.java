package com.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.form.UserFilterForm;
import com.company.form.CreatingUserForm;

public interface IUserService extends UserDetailsService {

	Page<UserDTO> getAllUsers(Pageable pageable, UserFilterForm filterForm);

	User getUserByID(int id);

	void createUser(CreatingUserForm form);

	boolean isUserExistsByUsername(String username);

	boolean isUserExistsByID(int id);

	public User getUserByUsername(String username);
}
