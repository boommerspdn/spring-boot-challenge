package com.sapondanai.spring_boot_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapondanai.spring_boot_challenge.dto.CreateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UserResponse;
import com.sapondanai.spring_boot_challenge.entity.User;
import com.sapondanai.spring_boot_challenge.exception.DuplicateFieldException;
import com.sapondanai.spring_boot_challenge.exception.GlobalExceptionHandler;
import com.sapondanai.spring_boot_challenge.exception.UserNotFoundException;
import com.sapondanai.spring_boot_challenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername("jsmith");
        user.setEmail("john.smith@example.com");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setCreatedAt(Instant.now());
        userResponse = new UserResponse(user);
    }

    // --- POST /api/users ---

    @Test
    void createUser_success_returns201() throws Exception {
        when(userService.createUser(any())).thenReturn(userResponse);

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("jsmith");
        request.setEmail("john.smith@example.com");
        request.setFirstName("John");
        request.setLastName("Smith");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("jsmith"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void createUser_missingFields_returns400() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.status").value(400));
    }

    @Test
    void createUser_duplicateUsername_returns409() throws Exception {
        when(userService.createUser(any())).thenThrow(new DuplicateFieldException("username", "jsmith"));

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("jsmith");
        request.setEmail("john.smith@example.com");
        request.setFirstName("John");
        request.setLastName("Smith");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.status").value(409))
                .andExpect(jsonPath("$.error.message").value("username already taken: jsmith"));
    }

    @Test
    void createUser_duplicateEmail_returns409() throws Exception {
        when(userService.createUser(any())).thenThrow(new DuplicateFieldException("email", "john.smith@example.com"));

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("jsmith");
        request.setEmail("john.smith@example.com");
        request.setFirstName("John");
        request.setLastName("Smith");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.status").value(409))
                .andExpect(jsonPath("$.error.message").value("email already taken: john.smith@example.com"));
    }

    // --- GET /api/users ---

    @Test
    void getAllUsers_returnsList() throws Exception {
        when(userService.getAllUsers(isNull(), any())).thenReturn(List.of(userResponse));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].username").value("jsmith"));
    }

    @Test
    void getAllUsers_emptyList_returnsEmptyArray() throws Exception {
        when(userService.getAllUsers(isNull(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    // --- GET /api/users/{id} ---

    @Test
    void getUserById_success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("jsmith"));
    }

    @Test
    void getUserById_notFound_returns404() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.status").value(404))
                .andExpect(jsonPath("$.error.message").value("User not found with id: 99"));
    }

    @Test
    void getUserById_nonNumericId_returns400() throws Exception {
        mockMvc.perform(get("/api/users/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.status").value(400));
    }
}
