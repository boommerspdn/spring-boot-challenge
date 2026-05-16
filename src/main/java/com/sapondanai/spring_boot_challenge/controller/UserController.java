package com.sapondanai.spring_boot_challenge.controller;

import com.sapondanai.spring_boot_challenge.dto.ApiResponse;
import com.sapondanai.spring_boot_challenge.dto.CreateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UpdateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UserResponse;
import com.sapondanai.spring_boot_challenge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.ok(userService.createUser(request));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(userService.getAllUsers(search, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return ApiResponse.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
