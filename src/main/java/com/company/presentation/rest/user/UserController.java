package com.company.presentation.rest.user;

import com.company.adaptor.database.form.CreatingUserForm;
import com.company.adaptor.database.form.UserFilterForm;
import com.company.domain.entity.User;
import com.company.domain.exception.ResourceNotFoundException;
import com.company.presentation.annotation.validation.UserIDExists;
import com.company.presentation.rest.user.response.UserDTO;
import com.company.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
