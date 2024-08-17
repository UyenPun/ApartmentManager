package com.company.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.company.entity.User;

public interface IUserService extends UserDetailsService {
	User getUserByUsername(String username);
}
