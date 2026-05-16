package com.sapondanai.spring_boot_challenge.service;

import com.sapondanai.spring_boot_challenge.dto.CreateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
}
