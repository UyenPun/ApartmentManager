package com.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.company.dto.UserDTO;
import com.company.entity.User;
import com.company.form.CreatingUserForm;
import com.company.form.UserFilterForm;
import com.company.repository.IUserRepository;
import com.company.specification.UserSpecification;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private ModelMapper modelMapper; // Ensure this is autowired

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString()));
    }

    @Override
    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User getUserByID(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable, UserFilterForm filterForm) {
        Specification<User> where = UserSpecification.buildWhere(filterForm);
        Page<User> entityPage = repository.findAll(where, pageable);

        List<UserDTO> dtos = modelMapper.map(entityPage.getContent(), new TypeToken<List<UserDTO>>() {}.getType());

        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    @Override
    public void createUser(CreatingUserForm form) {
        User user = modelMapper.map(form, User.class);
        repository.save(user);
    }

    @Override
    public boolean isUserExistsByUsername(String username) {
        return repository.findByUsername(username) != null;
    }

    @Override
    public boolean isUserExistsByID(int id) {
        return repository.findById(id).isPresent();
    }
}
