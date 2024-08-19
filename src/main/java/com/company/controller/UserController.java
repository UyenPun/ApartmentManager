package com.company.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.exception.ResourceNotFoundException;
import com.company.form.UserFilterForm;
import com.company.form.CreatingUserForm;
import com.company.service.IUserService;
import com.company.validation.UserIDExists;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "api/v1/users")
@Validated
@Log4j2
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ModelMapper modelMapper; // Add this to use modelMapper

	@GetMapping
	public Page<UserDTO> getAllUsers(Pageable pageable, UserFilterForm filterForm) {
		return userService.getAllUsers(pageable, filterForm);
	}

	@GetMapping(value = "/{id}")
	public UserDTO getUserByID(@PathVariable(name = "id") @UserIDExists Integer id) {
//		log.info("Fetching user with ID: {}", id); // Log the ID of the user being fetched
		User user = userService.getUserByID(id);
		if (user == null) {
			throw new ResourceNotFoundException("User not found with id " + id); // Ensure this exception class exists
		}
		return modelMapper.map(user, UserDTO.class); // Use modelMapper to map User to UserDTO
	}

	@PostMapping
	public void createUser(@RequestBody @Valid CreatingUserForm form) {
		userService.createUser(form);
	}
}
