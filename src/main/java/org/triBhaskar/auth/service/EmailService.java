package org.triBhaskar.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n\n" +
                "http://your-frontend-url/reset-password?token=" + token);

        try {
            mailSender.send(message);
            return true; // Email sent successfully
        } catch (MailException e) {
            // Log the exception (optional)
            System.err.println("Failed to send email: " + e.getMessage());
            return false; // Email not sent
        }
    }
}
