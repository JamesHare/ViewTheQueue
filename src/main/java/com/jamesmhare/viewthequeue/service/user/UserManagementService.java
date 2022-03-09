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

/**
 * Provides functionality for User Account Self-Service.
 *
 * @author James Hare
 */
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

    /**
     * Finds a Role by name.
     *
     * @param name the role name.
     * @return the Role with the given name.
     */
    public Role findRoleByName(final String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Adds a new Role.
     *
     * @param role a new Role.
     * @return the new Role as it was added to the datasource.
     */
    public Role addRole(final Role role) {
        return roleRepository.save(role);
    }

    /**
     * Finds a User by Email Address.
     *
     * @param email an Email Address.
     * @return the User with the given Email Address.
     */
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Adds a new User.
     *
     * @param user a new User.
     * @return the new User as it was added to the datasource.
     */
    public User addUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * Registers a new User.
     *
     * @param userDto the DTO representing a new User.
     * @return the new USer as it was added to the datasource.
     */
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

    /**
     * Updates a User.
     *
     * @param user the User to update.
     * @return the updated User.
     */
    public User updateUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * Soft deletes a User by disabling it and saving it to the datasource.
     *
     * @param user the User to soft delete.
     * @return the soft deleted User.
     */
    public User softDeleteUser(final User user) {
        user.setEnabled(false);
        return userRepository.save(user);
    }

    /**
     * Encodes a given password so that it can be saved in the datasource.
     *
     * @param password a password.
     * @return the encoded password.
     */
    public String encodePassword(final String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Adds a new User Account Verification object to the datasource.
     *
     * @param userAccountVerification a new User Account Verification object.
     * @return the new User Account Verification object.
     */
    public UserAccountVerification addUserAccountVerification(final UserAccountVerification userAccountVerification) {
        return userAccountVerificationRepository.save(userAccountVerification);
    }

    /**
     * Finds a User Account Verification given an ID.
     *
     * @param id the ID of a User Account Verification.
     * @return an Optional object containing either the User Account Verification object or an empty object if not found.
     */
    public Optional<UserAccountVerification> findUserAccountVerification(final UUID id) {
        return userAccountVerificationRepository.findById(id);
    }

    /**
     * Finds the User Account Verification object by Email Address.
     *
     * @param email an Email Address.
     * @return the User Account Verification object.
     */
    public Optional<UserAccountVerification> findUserAccountVerificationByEmail(final String email) {
        return userAccountVerificationRepository.findUserAccountVerificationByUserEmail(email);
    }

    /**
     * Deletes a User Account Verification object from the datasource.
     *
     * @param id the ID of the User Account Verification object to delete.
     */
    public void deleteUserAccountVerification(final UUID id) {
        userAccountVerificationRepository.deleteById(id);
    }

    /**
     * Adds a new Password Reset Token.
     *
     * @param passwordResetToken a new Password Reset Token.
     * @return the Password Reset Token as it was added.
     */
    public PasswordResetToken addPasswordResetToken(final PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    /**
     * Finds a Password Reset Token given an ID.
     *
     * @param id the ID of a Password Reset Token.
     * @return an Optional object containing either the Password Reset Token object or an empty object if not found.
     */
    public Optional<PasswordResetToken> findPasswordResetToken(final UUID id) {
        return passwordResetTokenRepository.findById(id);
    }

    /**
     * Deletes a Password Reset Token given an ID.
     *
     * @param id the ID of the Password Reset Token to delete.
     */
    public void deletePasswordResetToken(final UUID id) {
        passwordResetTokenRepository.deleteById(id);
    }

}
