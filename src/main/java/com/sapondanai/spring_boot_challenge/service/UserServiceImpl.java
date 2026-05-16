package com.sapondanai.spring_boot_challenge.service;

import com.sapondanai.spring_boot_challenge.dto.CreateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UpdateUserRequest;
import com.sapondanai.spring_boot_challenge.dto.UserResponse;
import com.sapondanai.spring_boot_challenge.entity.User;
import com.sapondanai.spring_boot_challenge.exception.DuplicateFieldException;
import com.sapondanai.spring_boot_challenge.exception.UserNotFoundException;
import com.sapondanai.spring_boot_challenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateFieldException("username", request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateFieldException("email", request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return new UserResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> getAllUsers(String search, Pageable pageable) {
        return userRepository.findBySearch(search, pageable)
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::new)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
            throw new DuplicateFieldException("username", request.getUsername());
        }
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateFieldException("email", request.getEmail());
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return new UserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
