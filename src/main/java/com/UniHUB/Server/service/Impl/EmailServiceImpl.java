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

    @Override
    public void sendDeactivationEmail(String toEmail, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("UniHUB Account Deactivation Notice");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "We are writing to inform you that your UniHUB account has been deactivated.\n\n" +
                            "Account Details:\n" +
                            "Email: %s\n" +
                            "Status: Deactivated\n\n" +
                            "You will no longer be able to access the UniHUB system with your current credentials.\n\n" +
                            "If you believe this is an error or need to reactivate your account, " +
                            "please contact the administration team.\n\n" +
                            "Best regards,\n" +
                            "UniHUB Administration Team",
                    userName, toEmail
            );

            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("Deactivation email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send deactivation email to: " + toEmail + ", Error: " + e.getMessage());
            throw new RuntimeException("Failed to send deactivation email: " + e.getMessage());
        }
    }

    @Override
    public void sendReactivationEmail(String toEmail, String userName, String newPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("UniHUB Account Reactivation - New Credentials");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Great news! Your UniHUB account has been reactivated.\n\n" +
                            "Your new login credentials are:\n" +
                            "Email: %s\n" +
                            "New Password: %s\n\n" +
                            "For security purposes, please log in and change your password as soon as possible.\n\n" +
                            "You can now access all UniHUB services with these credentials.\n\n" +
                            "Best regards,\n" +
                            "UniHUB Administration Team",
                    userName, toEmail, newPassword
            );

            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("Reactivation email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println("Failed to send reactivation email to: " + toEmail + ", Error: " + e.getMessage());
            throw new RuntimeException("Failed to send reactivation email: " + e.getMessage());
        }
    }
}
