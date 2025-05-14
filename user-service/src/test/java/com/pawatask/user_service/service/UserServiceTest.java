package com.pawatask.user_service.service;

import com.pawatask.user_service.dto.RegisterRequest;
import com.pawatask.user_service.exception.UsernameAlreadyExistsException;
import com.pawatask.user_service.model.User;
import com.pawatask.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void register_shouldCreateNewUser_whenUsernameIsAvailable() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("123");
        request.setFullName("Test User");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("hashed123");

        User savedUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("hashed123")
                .fullName("Test User")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("hashed123");
        assertThat(result.getFullName()).isEqualTo("Test User");

        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setPassword("123");
        request.setFullName("Already Exists");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessage("User with this username already exists");

        verify(userRepository).existsByUsername("existing");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }
}