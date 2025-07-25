package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendUserCredentials(String toEmail, String studentName, String temporaryPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Welcome to UniHUB - Your Account Details");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Welcome to UniHUB Student Management System!\n\n" +
                            "Your account has been successfully created. Here are your login credentials:\n\n" +
                            "Email: %s\n" +
                            "Temporary Password: %s\n\n" +
                            "Please log in and change your password for security purposes.\n\n" +
                            "Best regards,\n" +
                            "UniHUB Administration Team",
                    studentName, toEmail, temporaryPassword
            );

            message.setText(emailBody);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send email to: " + toEmail + ", Error: " + e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
