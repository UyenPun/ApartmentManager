package com.company.service;

import com.company.adaptor.database.form.CreatingUserForm;
import com.company.adaptor.database.form.UserFilterForm;
import com.company.adaptor.database.repository.IUserRepository;
import com.company.domain.entity.User;
import com.company.presentation.rest.user.response.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        String username = "johndoe";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(User.Role.ADMIN);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "johndoe";

        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetUserByUsername_Success() {
        // Arrange
        String username = "johndoe";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        User result = userService.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetUserByID_Success() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByID(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByID_NotFound() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserByID(userId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetAllUsers_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        UserFilterForm filterForm = new UserFilterForm();

        // Create a sample User entity
        User user = new User();
        user.setUsername("johndoe");

        // Create a Page of User entities
        Page<User> userPage = new PageImpl<>(List.of(user));

        // Create a list of UserDTOs that corresponds to the entities
        List<UserDTO> userDTOs = List.of(new UserDTO());

        // Mock the repository and modelMapper behaviors
        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(userPage);

        // Use correct TypeToken for modelMapper.map
        when(modelMapper.map(eq(userPage.getContent()), 
                             any(new TypeToken<List<UserDTO>>() {}.getType().getClass())))
            .thenReturn(userDTOs);

        // Act
        Page<UserDTO> result = userService.getAllUsers(pageable, filterForm);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(modelMapper, times(1)).map(eq(userPage.getContent()), 
                                          any(new TypeToken<List<UserDTO>>() {}.getType().getClass()));
    }

   
    @Test
    void testCreateUser_Success() {
        // Arrange
        CreatingUserForm form = new CreatingUserForm();
        form.setUsername("johndoe");
        form.setEmail("john.doe@example.com");
        form.setPassword("password");
        form.setRole(User.Role.RESIDENT.name()); // Set role as a string

        // Create a User entity that corresponds to the CreatingUserForm
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword());
        user.setRole(User.Role.RESIDENT); // Convert string role to enum directly

        // Mock the behavior of modelMapper and repository save
        when(modelMapper.map(form, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.createUser(form);

        // Assert
        verify(modelMapper, times(1)).map(form, User.class);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testIsUserExistsByUsername_Exists() {
        // Arrange
        String username = "johndoe";

        when(userRepository.findByUsername(username)).thenReturn(new User());

        // Act
        boolean exists = userService.isUserExistsByUsername(username);

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testIsUserExistsByUsername_NotExists() {
        // Arrange
        String username = "johndoe";

        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        boolean exists = userService.isUserExistsByUsername(username);

        // Assert
        assertFalse(exists);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testIsUserExistsByID_Exists() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // Act
        boolean exists = userService.isUserExistsByID(userId);

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testIsUserExistsByID_NotExists() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean exists = userService.isUserExistsByID(userId);

        // Assert
        assertFalse(exists);
        verify(userRepository, times(1)).findById(userId);
    }
}
