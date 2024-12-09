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

    public void sendPasswordResetEmail(String to, String token, String resetPwdUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n\n" +
                resetPwdUrl+"?token=" + token);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (MailException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to process password reset request");
        }
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP for Email Verification");
        message.setText("Your OTP for email verification is: " + otp);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (MailException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to process email verification request");
        }
    }
}
