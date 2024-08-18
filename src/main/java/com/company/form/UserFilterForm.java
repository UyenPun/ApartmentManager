package com.company.form;

import com.company.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserFilterForm {

	private String username;
	private String email;
	private User.Role role;

}
