package com.jamesmhare.viewthequeue.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;

/**
 * Provides a Service responsible for sending Emails across the Web Application.
 *
 * @author James Hare
 */
@Service
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(final JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends an email.
     *
     * @param to               the email recipient.
     * @param subject          the email subject.
     * @param messageBodyParts a List of paragraphs for the email body.
     * @throws Exception throws if there was an issue when trying to send the email.
     */
    public void sendEmail(final String to, final String subject, final List<String> messageBodyParts) throws Exception {
        final MimeMessage message = emailSender.createMimeMessage();
        message.setFrom("viewthequeue@gmail.com");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        final Multipart mimeMultipart = new MimeMultipart();
        for (final String messageBodyPart : messageBodyParts) {
            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent("<p>" + messageBodyPart + "</p>", "text/html");
            mimeMultipart.addBodyPart(mimeBodyPart);
        }

        // add salutation
        final MimeBodyPart valedictionMimeBodyPart = new MimeBodyPart();
        valedictionMimeBodyPart.setContent("<p>Best regards,</p>", "text/html");
        mimeMultipart.addBodyPart(valedictionMimeBodyPart);
        final MimeBodyPart teamNameMimeBodyPart = new MimeBodyPart();
        teamNameMimeBodyPart.setContent("<p>The View The Queue Applications Team</p>", "text/html");
        mimeMultipart.addBodyPart(teamNameMimeBodyPart);

        message.setContent(mimeMultipart);
        emailSender.send(message);
    }
}