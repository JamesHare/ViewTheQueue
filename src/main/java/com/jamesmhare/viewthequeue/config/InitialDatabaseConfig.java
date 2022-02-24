package com.jamesmhare.viewthequeue.config;

import com.jamesmhare.viewthequeue.model.user.Role;
import com.jamesmhare.viewthequeue.model.user.User;
import com.jamesmhare.viewthequeue.service.user.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * This is for testing locally and should not be shipped.
 */
@EnableTransactionManagement
@Configuration
@Slf4j
public class InitialDatabaseConfig {

    private final UserManagementService userManagementService;
    private final BCryptPasswordEncoder passwordEncoder;

    public InitialDatabaseConfig(final UserManagementService userManagementService,
                                 final BCryptPasswordEncoder passwordEncoder) {
        this.userManagementService = userManagementService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        try {
            Role userRole = userManagementService.findRoleByName("USER");
            Role adminRole = userManagementService.findRoleByName("ADMIN");

            if (userRole == null) {
                log.info("USER Role not found. Adding the USER Role to database.");
                userManagementService.addRole(Role.builder()
                        .name("USER")
                        .build());
            }

            if (adminRole == null) {
                log.info("ADMIN Role not found. Adding the ADMIN Role to database.");
                userManagementService.addRole(Role.builder()
                        .name("ADMIN")
                        .build());
            }

            log.info("USER and ADMIN Roles are present in the database.");

            userRole = userManagementService.findRoleByName("USER");
            adminRole = userManagementService.findRoleByName("ADMIN");

            final User testUser = userManagementService.addUser(User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .firstName("James")
                    .lastName("Hare")
                    .enabled(true)
                    .roles(Set.of(adminRole))
                    .build());

        } catch (Exception e) {
            log.error("An error occurred when trying to add USER and ADMIN roles to database.", e);
        }

    }

}
