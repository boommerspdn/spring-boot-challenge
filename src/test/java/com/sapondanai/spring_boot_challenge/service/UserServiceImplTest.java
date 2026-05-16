package com.sapondanai.spring_boot_challenge.service;

import com.sapondanai.spring_boot_challenge.dto.CreateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UserResponse;
import com.sapondanai.spring_boot_challenge.entity.User;
import com.sapondanai.spring_boot_challenge.exception.DuplicateFieldException;
import com.sapondanai.spring_boot_challenge.exception.UserNotFoundException;
import com.sapondanai.spring_boot_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User savedUser;
    private CreateUserRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequest();
        request.setUsername("jsmith");
        request.setEmail("john.smith@example.com");
        request.setFirstName("John");
        request.setLastName("Smith");

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("jsmith");
        savedUser.setEmail("john.smith@example.com");
        savedUser.setFirstName("John");
        savedUser.setLastName("Smith");
        savedUser.setCreatedAt(Instant.now());
    }

    // --- createUser ---

    @Test
    void createUser_success() {
        when(userRepository.existsByUsername("jsmith")).thenReturn(false);
        when(userRepository.existsByEmail("john.smith@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("jsmith");
        assertThat(response.getEmail()).isEqualTo("john.smith@example.com");
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Smith");
        assertThat(response.getCreatedAt()).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateUsername_throwsConflict() {
        when(userRepository.existsByUsername("jsmith")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateFieldException.class)
                .hasMessageContaining("username");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_duplicateEmail_throwsConflict() {
        when(userRepository.existsByUsername("jsmith")).thenReturn(false);
        when(userRepository.existsByEmail("john.smith@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateFieldException.class)
                .hasMessageContaining("email");

        verify(userRepository, never()).save(any());
    }

    // --- getAllUsers ---

    @Test
    void getAllUsers_returnsList() {
        Pageable pageable = PageRequest.of(0, 20);
        when(userRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(savedUser), pageable, 1));

        List<UserResponse> result = userService.getAllUsers(null, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("jsmith");
    }

    @Test
    void getAllUsers_emptyList() {
        Pageable pageable = PageRequest.of(0, 20);
        when(userRepository.findBySearch(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        List<UserResponse> result = userService.getAllUsers(null, pageable);

        assertThat(result).isEmpty();
    }

    // --- getUserById ---

    @Test
    void getUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        UserResponse response = userService.getUserById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("jsmith");
    }

    @Test
    void getUserById_notFound_throwsNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }
}
