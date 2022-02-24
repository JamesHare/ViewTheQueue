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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class UserManagementServiceTest {

    private final String roleName = "TEST";
    private final String email = "test@domain.com";
    private final String firstName = "test";
    private final String password = "test_passwd";
    private final UUID userAccountVerificationId = UUID.randomUUID();
    private final UUID passwordResetTokenId = UUID.randomUUID();
    private final Role role = Role.builder().roleId(1L).name(roleName).build();
    private User user;
    private UserDto userDto;
    private UserAccountVerification userAccountVerification;
    private PasswordResetToken passwordResetToken;
    private UserManagementService userManagementService;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private UserAccountVerificationRepository mockUserAccountVerificationRepository;
    @Mock
    private PasswordResetTokenRepository mockPasswordResetTokenRepository;
    @Mock
    private BCryptPasswordEncoder mockPasswordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userManagementService = new UserManagementService(mockUserRepository, mockRoleRepository,
                mockUserAccountVerificationRepository, mockPasswordResetTokenRepository, mockPasswordEncoder);
        user = User.builder()
                .email(email)
                .firstName(firstName)
                .password(password)
                .enabled(true)
                .roles(Set.of(role))
                .build();
        userDto = UserDto.builder()
                .emailAddress(email)
                .firstName(firstName)
                .password(password)
                .build();
        userAccountVerification = UserAccountVerification.builder().build();
        passwordResetToken = PasswordResetToken.builder().token(passwordResetTokenId).build();
    }

    @Test
    public void testFindRoleByName() {
        Mockito.when(mockRoleRepository.findByName(roleName)).thenReturn(role);
        final Role roleByName = userManagementService.findRoleByName(roleName);
        Assertions.assertEquals(role, roleByName);
    }

    @Test
    public void testAddRole() {
        Mockito.when(mockRoleRepository.save(role)).thenReturn(role);
        final Role savedRole = userManagementService.addRole(role);
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void testFindUserByEmail() {
        Mockito.when(mockUserRepository.findByEmail(email)).thenReturn(user);
        final User userByEmail = userManagementService.findUserByEmail(email);
        Assertions.assertEquals(user, userByEmail);
    }

    @Test
    public void testAddUser() {
        Mockito.when(mockUserRepository.save(user)).thenReturn(user);
        final User savedUser = userManagementService.addUser(user);
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void testRegisterNewUser() {
        Mockito.when(mockUserRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(mockRoleRepository.findByName("USER")).thenReturn(role);
        final User registeredUser = userManagementService.registerNewUser(userDto);
        Assertions.assertEquals(user, registeredUser);
    }

    @Test
    public void testUpdateUser() {
        Mockito.when(mockUserRepository.save(user)).thenReturn(user);
        final User updatedUser = userManagementService.updateUser(user);
        Assertions.assertEquals(user, updatedUser);
    }

    @Test
    public void testSoftDeleteUser() {
        Mockito.when(mockUserRepository.save(user)).thenReturn(user);
        final User softDeleteUser = userManagementService.softDeleteUser(user);
        Assertions.assertFalse(user.isEnabled());
        Assertions.assertEquals(user, softDeleteUser);
    }

    @Test
    public void testEncodePassword() {
        Mockito.when(mockPasswordEncoder.encode(password)).thenReturn(password);
        final String encodedPassword = userManagementService.encodePassword(password);
        Assertions.assertEquals(password, encodedPassword);
    }

    @Test
    public void testAddUserAccountVerification() {
        Mockito.when(mockUserAccountVerificationRepository.save(userAccountVerification)).thenReturn(userAccountVerification);
        final UserAccountVerification savedUserAccountVerification = userManagementService
                .addUserAccountVerification(userAccountVerification);
        Assertions.assertEquals(userAccountVerification, savedUserAccountVerification);
    }

    @Test
    public void testFindUserAccountVerification() {
        Mockito.when(mockUserAccountVerificationRepository.findById(userAccountVerificationId))
                .thenReturn(Optional.ofNullable(userAccountVerification));
        final Optional<UserAccountVerification> foundUserAccountVerification = userManagementService
                .findUserAccountVerification(userAccountVerificationId);
        Assertions.assertEquals(userAccountVerification, foundUserAccountVerification.get());
    }

    @Test
    public void testFindUserAccountVerificationByEmail() {
        Mockito.when(mockUserAccountVerificationRepository.findUserAccountVerificationByUserEmail(email))
                .thenReturn(Optional.ofNullable(userAccountVerification));
        final Optional<UserAccountVerification> foundUserAccountVerification = userManagementService
                .findUserAccountVerificationByEmail(email);
        Assertions.assertEquals(userAccountVerification, foundUserAccountVerification.get());
    }

    @Test
    public void testDeleteUserAccountVerification() {
        userManagementService.deleteUserAccountVerification(userAccountVerificationId);
        Mockito.verify(mockUserAccountVerificationRepository, Mockito.times(1)).deleteById(userAccountVerificationId);
    }

    @Test
    public void testAddPasswordResetToken() {
        Mockito.when(mockPasswordResetTokenRepository.save(passwordResetToken)).thenReturn(passwordResetToken);
        final PasswordResetToken addedPasswordResetToken = userManagementService.addPasswordResetToken(passwordResetToken);
        Assertions.assertEquals(passwordResetToken, addedPasswordResetToken);
    }

    @Test
    public void testFindPasswordResetToken() {
        Mockito.when(mockPasswordResetTokenRepository.findById(passwordResetTokenId)).thenReturn(Optional.ofNullable(passwordResetToken));
        final Optional<PasswordResetToken> addedPasswordResetToken = userManagementService.findPasswordResetToken(passwordResetTokenId);
        Assertions.assertEquals(passwordResetToken, addedPasswordResetToken.get());
    }

    @Test
    public void testDeletePasswordResetToken() {
        userManagementService.deletePasswordResetToken(passwordResetTokenId);
        Mockito.verify(mockPasswordResetTokenRepository, Mockito.times(1)).deleteById(passwordResetTokenId);
    }

}
