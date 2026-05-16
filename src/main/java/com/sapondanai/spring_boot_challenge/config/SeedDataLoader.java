package com.sapondanai.spring_boot_challenge.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapondanai.spring_boot_challenge.entity.User;
import com.sapondanai.spring_boot_challenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SeedDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedDataLoader.class);

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File seedFile = new File("data/users.json");
        if (!seedFile.exists()) {
            log.info("Seed file not found, skipping seed data load");
            return;
        }

        List<Map<String, String>> records = objectMapper.readValue(seedFile, new TypeReference<>() {});
        int loaded = 0;
        for (Map<String, String> record : records) {
            String username = record.get("username");
            String email = record.get("email");
            if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
                continue;
            }
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(record.get("firstName"));
            user.setLastName(record.get("lastName"));
            userRepository.save(user);
            loaded++;
        }
        log.info("Seed data: loaded {} user(s) from {}", loaded, seedFile.getPath());
    }
}
