package com.company.form;

import com.company.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

	@NotBlank
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotNull
	private User.Role role;
}
