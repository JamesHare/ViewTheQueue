package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.user.PasswordResetToken;
import com.jamesmhare.viewthequeue.model.user.User;
import com.jamesmhare.viewthequeue.model.user.UserAccountVerification;
import com.jamesmhare.viewthequeue.model.user.dto.UserDto;
import com.jamesmhare.viewthequeue.service.email.EmailService;
import com.jamesmhare.viewthequeue.service.user.UserManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class UserManagementControllerTest {

    private UserManagementController controller;

    @Mock
    private UserManagementService mockUserManagementService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private Model mockModel;

    private final String firstName = "test";
    private final String email = "test@domain.com";
    private final User user = User.builder().email(email).firstName(firstName).build();
    private final UserDto userDto = UserDto.builder().emailAddress("test@domain.com").password("test").build();
    private final UUID userToken = UUID.randomUUID();
    private final UUID resetToken = UUID.randomUUID();
    private final PasswordResetToken passwordResetToken = PasswordResetToken.builder().token(resetToken).user(user).build();
    private final String confirmationLink = "http://localhost:8080/user/confirm?token=" + userToken;
    private final String resetLink = "http://localhost:8080/user/reset?token=" + resetToken;
    private final UserAccountVerification userAccountVerification = UserAccountVerification.builder()
            .token(userToken).user(user).expiryDate(new Date(System.currentTimeMillis())).build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new UserManagementController(mockUserManagementService, mockEmailService);
    }

    @Test
    public void testShowUserRegistrationView() {
        String registrationView = controller.showUserRegistrationView(mockModel);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("userDto", new UserDto());
        Assertions.assertEquals("/user/user-registration-form", registrationView);
    }

    @Test
    public void testRegisterNewUser_UserAlreadyExists() {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(user);

        String registerNewUser = controller.registerNewUser(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("E-mail is already in use for another account. Please try a different E-mail Address."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("userDto", new UserDto());
        Assertions.assertEquals("/user/user-registration-form", registerNewUser);
    }

    @Test
    public void testRegisterNewUser_ExceptionThrownDuringSignUp() throws Exception {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(null);
        Mockito.when(mockUserManagementService.registerNewUser(userDto)).thenReturn(user);
        Mockito.when(mockUserManagementService.addUserAccountVerification(
                any(UserAccountVerification.class))).thenReturn(userAccountVerification);
        Mockito.doThrow(new Exception()).when(mockEmailService).sendEmail(any(), any(), any());

        String registerNewUser = controller.registerNewUser(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("Registration has failed. Please try again or <a href=\"/contact\">contact us</a> if you" +
                        " continue to have issues."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("userDto", new UserDto());
        Assertions.assertEquals("/user/user-registration-form", registerNewUser);
    }

    @Test
    public void testRegisterNewUser_Success() throws Exception {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(null);
        Mockito.when(mockUserManagementService.registerNewUser(userDto)).thenReturn(user);
        Mockito.when(mockUserManagementService.addUserAccountVerification(
                any(UserAccountVerification.class))).thenReturn(userAccountVerification);

        String registerNewUser = controller.registerNewUser(userDto, mockModel);

        Mockito.verify(mockEmailService, Mockito.times(1)).sendEmail(
                email,
                "Confirm Your Email Address for Your View The Queue Account",
                List.of("Hi test!",
                        "You are receiving this message because you recently used this email address to sign up to use View The Queue!",
                        "Please click the following link to confirm your Email Address: ",
                        "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>",
                        "<i>Note: this confirmation link will expire in 24 hours. Please confirm your account as soon as possible.")
        );
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("We sent a link to your email address to activate your account. Your confirmation link" +
                        " will expire in 24 hours."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("userDto", new UserDto());
        Assertions.assertEquals("/user/user-registration-form", registerNewUser);
    }

    @Test
    public void testShowAccountConfirmationView_TokenIsNull() {
        String accountConfirmationView = controller.showAccountConfirmationView(null, mockModel);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of("Sorry, your" +
                        " confirmation token is invalid or has expired. Please sign up again."));
        Assertions.assertEquals("generic-alerts-view", accountConfirmationView);
    }

    @Test
    public void testShowAccountConfirmationView_UserAccountVerificationIsEmpty() {
        Mockito.when(mockUserManagementService.findUserAccountVerification(userToken)).thenReturn(Optional.empty());
        String accountConfirmationView = controller.showAccountConfirmationView(userToken, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Sorry, your confirmation token is invalid."));
        Assertions.assertEquals("generic-alerts-view", accountConfirmationView);
    }

    @Test
    public void testShowAccountConfirmationView_TokenHasNotExpired() {
        userAccountVerification.setExpiryDate(new Date(System.currentTimeMillis() + 60000));
        Mockito.when(mockUserManagementService.findUserAccountVerification(userToken)).thenReturn(Optional.of(userAccountVerification));
        String accountConfirmationView = controller.showAccountConfirmationView(userToken, mockModel);

        Mockito.verify(mockUserManagementService, Mockito.times(1)).updateUser(any(User.class));
        Mockito.verify(mockUserManagementService, Mockito.times(1)).deleteUserAccountVerification(userToken);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "You have successfully confirmed your account. Please <a href=\"/login\">click here</a> to login."));
        Assertions.assertEquals("generic-alerts-view", accountConfirmationView);
    }

    @Test
    public void testShowAccountConfirmationView_TokenHasExpired() {
        userAccountVerification.setExpiryDate(new Date(System.currentTimeMillis() - 1));
        Mockito.when(mockUserManagementService.findUserAccountVerification(userToken)).thenReturn(Optional.of(userAccountVerification));
        String accountConfirmationView = controller.showAccountConfirmationView(userToken, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Sorry, your confirmation token has expired. <a href=\"/user/resend-confirmation\">Click here</a>" +
                        " to send a new confirmation token to your email address."));
        Assertions.assertEquals("generic-alerts-view", accountConfirmationView);
    }

    @Test
    public void testGetEmailConfirmationResendView() {
        String emailConfirmationResendView = controller.getEmailConfirmationResendView(mockModel);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("userDto", UserDto.builder().build());
        Assertions.assertEquals("/user/email-confirmation-resend", emailConfirmationResendView);
    }

    @Test
    public void testResendEmailConfirmation_EmailAddressNotFound() {
        Mockito.when(mockUserManagementService.findUserAccountVerificationByEmail(email)).thenReturn(Optional.empty());

        String resendEmailConfirmation = controller.resendEmailConfirmation(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with a new confirmation link."));
        Assertions.assertEquals("/user/email-confirmation-resend", resendEmailConfirmation);
    }

    @Test
    public void testResendEmailConfirmation_EmailAddressFound() throws Exception {
        Mockito.when(mockUserManagementService.findUserAccountVerificationByEmail(email)).thenReturn(Optional.of(userAccountVerification));

        String resendEmailConfirmation = controller.resendEmailConfirmation(userDto, mockModel);

        Mockito.verify(mockEmailService, Mockito.times(1)).sendEmail(
                "test@domain.com",
                "Confirm Your Email Address for Your View The Queue Account",
                List.of("Hi test!",
                        "You are receiving this message because you recently used this email address to sign up to use View The Queue!",
                        "Please click the following link to confirm your Email Address: ",
                        "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>",
                        "<i>Note: this confirmation link will expire in 24 hours. Please confirm your account as soon as possible."));
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with a new confirmation link."));
        Assertions.assertEquals("/user/email-confirmation-resend", resendEmailConfirmation);
    }

    @Test
    public void testResendEmailConfirmation_ExceptionThrownDuringResend() throws Exception {
        userAccountVerification.setExpiryDate(new Date(System.currentTimeMillis()));
        Mockito.when(mockUserManagementService.findUserAccountVerificationByEmail(email)).thenReturn(Optional.of(userAccountVerification));
        Mockito.doThrow(new Exception()).when(mockEmailService).sendEmail(any(), any(), any());

        String resendEmailConfirmation = controller.resendEmailConfirmation(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with a new confirmation link."));
        Assertions.assertEquals("/user/email-confirmation-resend", resendEmailConfirmation);
    }

    @Test
    public void testGetResetPasswordRequestView() {
        String resetPasswordRequestView = controller.getResetPasswordRequestView(mockModel);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("userDto", UserDto.builder().build());
        Assertions.assertEquals("/user/request-password-reset", resetPasswordRequestView);
    }

    @Test
    public void testRequestPasswordReset_UserNotFound() {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(null);

        String resetPasswordRequestView = controller.requestPasswordReset(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with instructions to reset your password."));
        Assertions.assertEquals("/user/request-password-reset", resetPasswordRequestView);
    }

    @Test
    public void testRequestPasswordReset_UserFound() throws Exception {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(user);
        Mockito.when(mockUserManagementService.addPasswordResetToken(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        String resetPasswordRequestView = controller.requestPasswordReset(userDto, mockModel);

        Mockito.verify(mockEmailService, Mockito.times(1)).sendEmail(
                "test@domain.com",
                "A Request was Made to Change Your View The Queue Password",
                List.of("Hi test!",
                        "You are receiving this message because a request was made to reset the password associated to your View The Queue Account.",
                        "Please click the following link to set a new password: ",
                        "<a href=\"" + resetLink + "\">" + resetLink + "</a>",
                        "<i>Note: If you did not initiate this request, you can simply ignore this message."
                )
        );
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with instructions to reset your password."));
        Assertions.assertEquals("/user/request-password-reset", resetPasswordRequestView);
    }

    @Test
    public void testRequestPasswordReset_ExceptionThrownDuringReset() throws Exception {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(user);
        Mockito.when(mockUserManagementService.addPasswordResetToken(any(PasswordResetToken.class))).thenReturn(passwordResetToken);
        Mockito.doThrow(new Exception()).when(mockEmailService).sendEmail(any(), any(), any());

        String resetPasswordRequestView = controller.requestPasswordReset(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with instructions to reset your password."));
        Assertions.assertEquals("/user/request-password-reset", resetPasswordRequestView);
    }

    @Test
    public void testGetResetPasswordView_TokenIsNull() {
        String resetPasswordRequestView = controller.getResetPasswordView(null, mockModel);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("Sorry, your token is invalid or has expired."));
        Assertions.assertEquals("generic-alerts-view", resetPasswordRequestView);
    }

    @Test
    public void testGetResetPasswordView_PasswordResetTokenIsEmpty() {
        Mockito.when(mockUserManagementService.findPasswordResetToken(resetToken)).thenReturn(Optional.empty());

        String resetPasswordRequestView = controller.getResetPasswordView(resetToken, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("Sorry, your token is invalid or has expired."));
        Assertions.assertEquals("generic-alerts-view", resetPasswordRequestView);
    }

    @Test
    public void testGetResetPasswordView_Success() {
        Mockito.when(mockUserManagementService.findPasswordResetToken(resetToken)).thenReturn(Optional.of(passwordResetToken));

        String resetPasswordRequestView = controller.getResetPasswordView(resetToken, mockModel);

        Mockito.verify(mockUserManagementService, Mockito.times(1)).deletePasswordResetToken(resetToken);
        Assertions.assertEquals("/user/password-reset", resetPasswordRequestView);
    }

    @Test
    public void testResetUserPassword_UserNotFound() {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(null);

        String resetPasswordRequestView = controller.resetUserPassword(userDto, mockModel);

        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", false);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("Your password could not be updated at this time. Please try again later."));
        Assertions.assertEquals("/user/password-reset", resetPasswordRequestView);
    }

    @Test
    public void testResetUserPassword_UserFound() {
        Mockito.when(mockUserManagementService.findUserByEmail(email)).thenReturn(user);

        String resetPasswordRequestView = controller.resetUserPassword(userDto, mockModel);

        Mockito.verify(mockUserManagementService, Mockito.times(1)).updateUser(user);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("success", true);
        Mockito.verify(mockModel, Mockito.times(1)).addAttribute("messages",
                List.of("Your password has been updated. Please login now."));
        Assertions.assertEquals("/user/password-reset", resetPasswordRequestView);
    }

}
