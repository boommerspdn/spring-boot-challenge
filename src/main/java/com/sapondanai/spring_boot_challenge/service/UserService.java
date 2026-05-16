package com.sapondanai.spring_boot_challenge.service;

import com.sapondanai.spring_boot_challenge.dto.CreateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UpdateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers(String search, Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}
