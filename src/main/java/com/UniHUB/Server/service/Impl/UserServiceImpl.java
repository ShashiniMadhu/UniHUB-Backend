package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.UserDAO;
import com.UniHUB.Server.dto.*;
import com.UniHUB.Server.service.UserService;
import com.UniHUB.Server.service.EmailService;
import com.UniHUB.Server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // ✅ Correct constructor
    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserResponseDTO login(UserLoginDTO loginDTO) {
        UserDTO user = userDAO.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Integer studentId = null;
        Integer lecturerId = null;

        if (user.getRole().equalsIgnoreCase("student")) {
            studentId = userDAO.findStudentIdByUserId(user.getUserId());
        } else if (user.getRole().equalsIgnoreCase("lecturer")) {
            lecturerId = userDAO.findLecturerIdByUserId(user.getUserId());
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole(),
                user.getUserId(),
                studentId,
                lecturerId
        );

        return new UserResponseDTO(
                user.getUserId(),
                user.getFName(),
                user.getEmail(),
                user.getRole(),
                token,
                studentId,
                lecturerId
        );
    }

    @Override
    public void generateResetToken(ForgotPasswordRequest request) {
        Optional<UserDTO> userDTO = userDAO.findByEmail(request.getEmail()); // ✅ FIXED
        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusHours(1)); // ✅ FIXED
            userDAO.save(user);


            String resetLink = "http://localhost:5173/reset-password?token=" + token;

            // ✅ Send email instead of printing
            emailService.sendpasswordResetEmail(user.getEmail(), user.getFName(), resetLink);

            System.out.println("Reset link sent to email: " + resetLink);
        } else {
            throw new RuntimeException("No user found with this email");
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        Optional<UserDTO> userDTO = userDAO.findByResetToken(request.getToken());
        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();

            // Check for null expiry to prevent NullPointerException
            if (user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Token Expired or Invalid!");
            }


            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            System.out.println("New password hash: " + user.getPassword());

            user.setResetToken(null);
            user.setTokenExpiry(null);
            userDAO.save(user);

        } else {
            throw new RuntimeException("Invalid Token!");
        }
    }
}
