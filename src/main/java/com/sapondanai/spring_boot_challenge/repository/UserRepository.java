package com.sapondanai.spring_boot_challenge.repository;

import com.sapondanai.spring_boot_challenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
