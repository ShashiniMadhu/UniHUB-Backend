package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.AdminDAO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.service.AdminService;
import com.UniHUB.Server.service.EmailService;
import com.UniHUB.Server.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDAO adminDAO;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordUtil passwordUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        try {
            // Generate random password
            String temporaryPassword = passwordUtil.generateRandomPassword();

            // Hash the password before storing in database
            String hashedPassword = passwordEncoder.encode(temporaryPassword);
            userDTO.setPassword(hashedPassword);

            // Create user in database (with hashed password)
            UserDTO createdUser = adminDAO.createUser(userDTO);

            // Send email with credentials (plain password for user)
            emailService.sendUserCredentials(
                    createdUser.getEmail(),
                    createdUser.getFName() + " " + createdUser.getLName(),
                    temporaryPassword
            );

            // Don't return password in response for security
            createdUser.setPassword(null);

            return createdUser;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }
}