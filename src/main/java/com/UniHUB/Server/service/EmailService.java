package com.UniHUB.Server.service;

public interface EmailService {

    void sendUserCredentials(String toEmail, String studentName, String temporaryPassword);
    void sendDeactivationEmail(String toEmail, String userName);
    void sendReactivationEmail(String toEmail, String userName, String newPassword);

    void sendpasswordResetEmail(String toEmail,String username,String resetLink);
}
