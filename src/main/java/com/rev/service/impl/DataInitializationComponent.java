package com.rev.service.impl;

import com.rev.domain.USER_ROLE;
import com.rev.modal.User;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminUsername = "revathibakkiyaraj49@gmail.com";

        if(userRepository.findByEmail(adminUsername)==null){
            User adminUser = new User();

            adminUser.setPassword(passwordEncoder.encode("Revathi@@24"));
            adminUser.setFullName("Revathi");
            adminUser.setEmail(adminUsername);
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);

            userRepository.save(adminUser);
        }
    }
}
