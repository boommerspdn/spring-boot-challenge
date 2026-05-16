package com.sapondanai.spring_boot_challenge.dto;

import com.sapondanai.spring_boot_challenge.entity.User;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final Instant createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.createdAt = user.getCreatedAt();
    }
}
