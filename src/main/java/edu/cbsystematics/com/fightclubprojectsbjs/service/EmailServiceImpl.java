package edu.cbsystematics.com.fightclubprojectsbjs.service;

import edu.cbsystematics.com.fightclubprojectsbjs.exception.EmailSendingException;
import edu.cbsystematics.com.fightclubprojectsbjs.model.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    // Sends an email with the provided mail details
    @Override
    public void sendEmail(Mail mail) {
        try {
            // Create a new MimeMessage for sending emails
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(mail.getModel());
            String html = templateEngine.process("email/email-template", context);

            // Set email details
            helper.setTo(mail.getTo());
            helper.setText(html, true); // Set HTML content to support a rich email format
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            // Send the email
            emailSender.send(message);
            logger.info("Verification Email has been sent");
        } catch (MessagingException ex) {
            logger.error("Error sending a verification email");
            throw new EmailSendingException("Failed to send verification email", ex);
        }
    }

}




