package com.jamesmhare.viewthequeue.controller;

import com.jamesmhare.viewthequeue.model.user.PasswordResetToken;
import com.jamesmhare.viewthequeue.model.user.User;
import com.jamesmhare.viewthequeue.model.user.UserAccountVerification;
import com.jamesmhare.viewthequeue.model.user.dto.UserDto;
import com.jamesmhare.viewthequeue.service.email.EmailService;
import com.jamesmhare.viewthequeue.service.user.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("user")
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final EmailService emailService;

    public UserManagementController(final UserManagementService userManagementService,
                                    final EmailService emailService) {
        this.userManagementService = userManagementService;
        this.emailService = emailService;
    }

    @GetMapping("register")
    public String showUserRegistrationView(final Model model) {
        model.addAttribute("userDto", new UserDto());
        return "/user/user-registration-form";
    }

    @PostMapping("register")
    public String registerNewUser(final UserDto userDto, final Model model) {
        if (userManagementService.findUserByEmail(userDto.getEmailAddress()) != null) {
            log.warn("A user tried to sign up using an email address which is already in use for another account = {}", userDto.getEmailAddress());
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("E-mail is already in use for another account. Please try a different E-mail Address."));
            return showUserRegistrationView(model);
        }


        try {
            final User user = userManagementService.registerNewUser(userDto);
            log.info("Registered new user = {}", user.toString());

            final Date twentyFourHoursFromNow = new Date(System.currentTimeMillis() + 86400000);
            UserAccountVerification userAccountVerification = userManagementService
                    .addUserAccountVerification(UserAccountVerification.builder()
                            .expiryDate(twentyFourHoursFromNow)
                            .user(user)
                            .build());

            log.info("Sending user registration validation email to new user = {}", user);
            sendConfirmationEmail(userAccountVerification.getToken(), user.getEmail(), user.getFirstName());
            log.info("Successfully sent user registration validation email to new user = {}", user);

            model.addAttribute("success", true);
            model.addAttribute("messages", List.of("We sent a link to your email address to activate" +
                    " your account. Your confirmation link will expire in 24 hours."));
            return showUserRegistrationView(model);
        } catch (final Exception e) {
            log.error("An exception occurred when trying to register a new user = {}", userDto, e);
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("Registration has failed. Please try again or" +
                    " <a href=\"/contact\">contact us</a> if you continue to have issues."));
            return showUserRegistrationView(model);
        }
    }

    @GetMapping("confirm")
    public String showAccountConfirmationView(@RequestParam("token") final UUID token, final Model model) {
        if (token == null) {
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("Sorry, your confirmation token is invalid or has expired. Please" +
                    " sign up again."));
            return "generic-alerts-view";
        }

        final Optional<UserAccountVerification> userAccountVerification = userManagementService.findUserAccountVerification(token);
        if (userAccountVerification.isEmpty()) {
            // the token is not valid and does not have a user account attached to it.
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of(
                    "Sorry, your confirmation token is invalid."));
        } else if (new Date(System.currentTimeMillis()).before(userAccountVerification.get().getExpiryDate())) {
            // enable the user and show a success message
            final User userToEnable = userAccountVerification.get().getUser();
            userToEnable.setEnabled(true);
            userManagementService.updateUser(userToEnable);
            log.info("User has been enabled = {}", userToEnable);
            userManagementService.deleteUserAccountVerification(userAccountVerification.get().getToken());
            log.info("Deleting User Account Verification Object = {}", userAccountVerification);

            model.addAttribute("success", true);
            model.addAttribute("messages", List.of(
                    "You have successfully confirmed your account. Please <a href=\"/login\">click here</a> to login."));
        } else {
            // the users token has expired, direct them to send another token.
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of(
                    "Sorry, your confirmation token has expired. <a href=\"/user/resend-confirmation\">Click here</a>" +
                            " to send a new confirmation token to your email address."));
        }
        return "generic-alerts-view";
    }

    @GetMapping("resend-confirmation")
    public String getEmailConfirmationResendView(final Model model) {
        model.addAttribute("userDto", new UserDto());
        return "/user/email-confirmation-resend";
    }

    @PostMapping("resend-confirmation")
    public String resendEmailConfirmation(final UserDto userDto, final Model model) {
        final Optional<UserAccountVerification> userAccountVerification = userManagementService.findUserAccountVerificationByEmail(userDto.getEmailAddress());

        if (userAccountVerification.isPresent()) {
            try {
                // extend the user expiration token
                final Date twentyFourHoursFromNow = new Date(System.currentTimeMillis() + 86400000);
                final UserAccountVerification userAccountVerificationToExtend = userAccountVerification.get();
                userAccountVerificationToExtend.setExpiryDate(twentyFourHoursFromNow);
                userManagementService.addUserAccountVerification(userAccountVerificationToExtend);

                // send confirmation email
                final UUID token = userAccountVerificationToExtend.getToken();
                final User user = userAccountVerificationToExtend.getUser();
                log.info("Sending user confirmation validation email to existing user = {}", user);
                sendConfirmationEmail(token, user.getEmail(), user.getFirstName());
                log.info("Successfully sent user confirmation validation email to existing user = {}", user);
            } catch (final Exception e) {
                log.error("An error occurred when trying to resend confirmation email for existing user = {}", userDto);
            }
        }

        model.addAttribute("success", true);
        model.addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with a new confirmation link."));
        return "/user/email-confirmation-resend";
    }

    @GetMapping("request-password-reset")
    public String getResetPasswordRequestView(final Model model) {
        model.addAttribute("userDto", new UserDto());
        return "/user/request-password-reset";
    }

    @PostMapping("request-password-reset")
    public String requestPasswordReset(final UserDto userDto, final Model model) {
        final User user = userManagementService.findUserByEmail(userDto.getEmailAddress());
        if (user != null) {
            try {
                // save password reset token for user found with email address
                final Date twentyFourHoursFromNow = new Date(System.currentTimeMillis() + 86400000);
                final PasswordResetToken passwordResetToken = userManagementService.addPasswordResetToken(PasswordResetToken.builder()
                        .user(user)
                        .expiryDate(twentyFourHoursFromNow)
                        .build());

                // send email to email address to reset password
                log.info("Sending password reset email to user = {}", user);
                sendPasswordResetEmail(passwordResetToken.getToken(), user.getEmail(), user.getFirstName());
                log.info("Successfully sent password reset email to user = {}", user);
            } catch (final Exception e) {
                log.error("An error occurred when trying to send password reset email to user = {}", userDto);
            }
        }

        model.addAttribute("success", true);
        model.addAttribute("messages", List.of(
                "Your submissions has been received. If we have an account matching your email address, you will" +
                        " receive an email with instructions to reset your password."));
        return "/user/request-password-reset";
    }

    @GetMapping("reset")
    public String getResetPasswordView(@RequestParam("token") final UUID token, final Model model) {
        if (token == null) {
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("Sorry, your token is invalid or has expired."));
            return "generic-alerts-view";
        }

        final Optional<PasswordResetToken> passwordResetToken = userManagementService.findPasswordResetToken(token);
        if (passwordResetToken.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("Sorry, your token is invalid or has expired."));
            return "generic-alerts-view";
        }

        final User user = passwordResetToken.get().getUser();
        final UserDto userDto = UserDto.builder()
                .emailAddress(user.getEmail())
                .build();

        // delete password reset token to invalidate future attempts to reset the password.
        userManagementService.deletePasswordResetToken(token);

        model.addAttribute("userDto", userDto);
        return "/user/password-reset";
    }

    @PostMapping("reset")
    public String resetUserPassword(final UserDto userDto, final Model model) {
        final User foundUser = userManagementService.findUserByEmail(userDto.getEmailAddress());
        if (foundUser != null) {
            foundUser.setPassword(userManagementService.encodePassword(userDto.getPassword()));
            userManagementService.updateUser(foundUser);
            model.addAttribute("success", true);
            model.addAttribute("messages", List.of("Your password has been updated. Please login now."));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("messages", List.of("Your password could not be updated at this time. Please" +
                    " try again later."));
        }
        return "/user/password-reset";
    }

    //------------------------------ HELPER METHODS ------------------------------//

    private void sendConfirmationEmail(final UUID token, final String email, final String firstName) throws Exception {
        final String confirmationLink = "http://localhost:8080/user/confirm?token=" + token.toString();

        emailService.sendEmail(
                email,
                "Confirm Your Email Address for Your View The Queue Account",
                List.of("Hi " + firstName + "!",
                        "You are receiving this message because you recently used this email address to sign up to use View The Queue!",
                        "Please click the following link to confirm your Email Address: ",
                        "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>",
                        "<i>Note: this confirmation link will expire in 24 hours. Please confirm your account as soon as possible."
                )
        );
    }

    private void sendPasswordResetEmail(final UUID token, final String email, final String firstName) throws Exception {
        final String confirmationLink = "http://localhost:8080/user/reset?token=" + token.toString();

        emailService.sendEmail(
                email,
                "A Request was Made to Change Your View The Queue Password",
                List.of("Hi " + firstName + "!",
                        "You are receiving this message because a request was made to reset the password associated to your View The Queue Account.",
                        "Please click the following link to set a new password: ",
                        "<a href=\"" + confirmationLink + "\">" + confirmationLink + "</a>",
                        "<i>Note: If you did not initiate this request, you can simply ignore this message."
                )
        );
    }

}
