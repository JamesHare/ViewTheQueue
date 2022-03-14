package com.jamesmhare.viewthequeue.service.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * A class containing Test Cases for the {@link EmailService}.
 *
 * @author James Hare
 */
public class EmailServiceTest {

    private EmailService emailService;

    @Mock
    private JavaMailSender mockJavaMailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(mockJavaMailSender);
    }

    @Test
    public void testSendEmail() throws Exception {
        final String to = "test@domain.com";
        final String subject = "test subject";
        final List<String> messageBodyParts = List.of(
                "Body Part One",
                "Body Part Two"
        );
        final MimeMessage mockMimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(mockJavaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        emailService.sendEmail(to, subject, messageBodyParts);

        Mockito.verify(mockMimeMessage, Mockito.times(1)).setContent(any(MimeMultipart.class));
        Mockito.verify(mockJavaMailSender, Mockito.times(1)).send(mockMimeMessage);
    }

}
