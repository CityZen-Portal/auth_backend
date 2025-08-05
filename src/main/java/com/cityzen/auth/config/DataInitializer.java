package com.cityzen.auth.config;

import com.cityzen.auth.entity.User;
import com.cityzen.auth.enums.Role;
import com.cityzen.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new User();
                admin.setUserName("admin");
                admin.setEmail("admin@admin.com");
                admin.setPassword(encoder.encode("admin@admin.com"));
                admin.setRoles(Set.of(Role.ADMIN));
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("user@user.com").isEmpty()) {
                User user = new User();
                user.setUserName("user");
                user.setEmail("user@user.com");
                user.setPassword(encoder.encode("user@user.com"));
                user.setRoles(Set.of(Role.CITIZEN));
                userRepository.save(user);
            }

            if (userRepository.findByEmail("staff@staff.com").isEmpty()) {
                User staff = new User();
                staff.setUserName("staff");
                staff.setEmail("staff@staff.com");
                staff.setPassword(encoder.encode("staff@staff.com"));
                staff.setRoles(Set.of(Role.STAFF));
                userRepository.save(staff);
            }
        };
    }
}