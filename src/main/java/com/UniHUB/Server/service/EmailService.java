package com.UniHUB.Server.service;

public interface EmailService {

    void sendUserCredentials(String toEmail, String studentName, String temporaryPassword);

}
