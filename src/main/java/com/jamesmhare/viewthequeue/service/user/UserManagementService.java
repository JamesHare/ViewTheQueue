package com.jamesmhare.viewthequeue.service.user;

import com.jamesmhare.viewthequeue.model.repo.PasswordResetTokenRepository;
import com.jamesmhare.viewthequeue.model.repo.RoleRepository;
import com.jamesmhare.viewthequeue.model.repo.UserAccountVerificationRepository;
import com.jamesmhare.viewthequeue.model.repo.UserRepository;
import com.jamesmhare.viewthequeue.model.user.PasswordResetToken;
import com.jamesmhare.viewthequeue.model.user.Role;
import com.jamesmhare.viewthequeue.model.user.User;
import com.jamesmhare.viewthequeue.model.user.UserAccountVerification;
import com.jamesmhare.viewthequeue.model.user.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserAccountVerificationRepository userAccountVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserManagementService(final UserRepository userRepository,
                                 final RoleRepository roleRepository,
                                 final UserAccountVerificationRepository userAccountVerificationRepository,
                                 final PasswordResetTokenRepository passwordResetTokenRepository,
                                 final BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userAccountVerificationRepository = userAccountVerificationRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Role findRoleByName(final String name) {
        return roleRepository.findByName(name);
    }

    public Role addRole(final Role role) {
        return roleRepository.save(role);
    }

    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User addUser(final User user) {
        return userRepository.save(user);
    }

    public User registerNewUser(final UserDto userDto) {
        return addUser(User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmailAddress())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(Set.of(findRoleByName("USER")))
                .enabled(false)
                .build());
    }

    public User updateUser(final User user) {
        return userRepository.save(user);
    }

    public User softDeleteUser(final User user) {
        user.setEnabled(false);
        return userRepository.save(user);
    }

    public String encodePassword(final String password) {
        return passwordEncoder.encode(password);
    }

    public UserAccountVerification addUserAccountVerification(final UserAccountVerification userAccountVerification) {
        return userAccountVerificationRepository.save(userAccountVerification);
    }

    public Optional<UserAccountVerification> findUserAccountVerification(final UUID id) {
        return userAccountVerificationRepository.findById(id);
    }

    public Optional<UserAccountVerification> findUserAccountVerificationByEmail(final String email) {
        return userAccountVerificationRepository.findUserAccountVerificationByUserEmail(email);
    }

    public void deleteUserAccountVerification(final UUID id) {
        userAccountVerificationRepository.deleteById(id);
    }

    public PasswordResetToken addPasswordResetToken(final PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    public Optional<PasswordResetToken> findPasswordResetToken(final UUID id) {
        return passwordResetTokenRepository.findById(id);
    }

    public void deletePasswordResetToken(final UUID id) {
        passwordResetTokenRepository.deleteById(id);
    }

}
