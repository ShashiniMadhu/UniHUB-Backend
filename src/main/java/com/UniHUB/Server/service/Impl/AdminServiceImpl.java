package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.AdminDAO;
import com.UniHUB.Server.dto.SiteAnnouncementDTO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.service.AdminService;
import com.UniHUB.Server.service.EmailService;
import com.UniHUB.Server.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<UserDTO> viewStudents() {
        List<UserDTO> students = adminDAO.viewStudents();
        return students;
    }

    @Override
    public List<UserDTO> viewLecturers() {
        List<UserDTO> lecturers = adminDAO.viewLecturers();
        return lecturers;
    }

    @Override
    public SiteAnnouncementDTO createSiteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO) {
        SiteAnnouncementDTO announcement = adminDAO.createSiteAnnouncement(siteAnnouncementDTO);
        return announcement;
    }

    @Override
    public List<SiteAnnouncementDTO> viewAnnouncements() {
        List<SiteAnnouncementDTO> announcementS = adminDAO.viewAnnouncements();
        return announcementS;
    }

    @Override
    public SiteAnnouncementDTO deleteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO) {
        SiteAnnouncementDTO announcement = adminDAO.deleteAnnouncement(siteAnnouncementDTO);
        return announcement;
    }

    @Override
    public SiteAnnouncementDTO updateAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO) {
        SiteAnnouncementDTO announcement = adminDAO.updateAnnouncement(siteAnnouncementDTO);
        return announcement;
    }

    @Override
    public UserDTO deactivateUser(UserDTO userDTO) {
        try {
            // Validate input
            if (userDTO.getUserId() == null) {
                throw new RuntimeException("User ID is required for deactivation");
            }

            // Get user details before deactivation for email
            UserDTO userToDeactivate = adminDAO.getUserById(userDTO.getUserId());

            // Deactivate user in database
            UserDTO deactivatedUser = adminDAO.deactivateUser(userDTO);

            // Send deactivation email
            String fullName = userToDeactivate.getFName() + " " +
                    (userToDeactivate.getLName() != null ? userToDeactivate.getLName() : "");
            emailService.sendDeactivationEmail(
                    userToDeactivate.getEmail(),
                    fullName.trim()
            );

            return deactivatedUser;

        } catch (Exception e) {
            throw new RuntimeException("Failed to deactivate user: " + e.getMessage());
        }
    }

    @Override
    public UserDTO reactivateUser(UserDTO userDTO) {
        try {
            // Validate input
            if (userDTO.getUserId() == null) {
                throw new RuntimeException("User ID is required for reactivation");
            }

            // Get user details before reactivation for email
            UserDTO userToReactivate = adminDAO.getUserById(userDTO.getUserId());

            // Generate new temporary password
            String newTemporaryPassword = passwordUtil.generateRandomPassword();

            // Hash the password before storing in database
            String hashedPassword = passwordEncoder.encode(newTemporaryPassword);
            userDTO.setPassword(hashedPassword);

            // Reactivate user in database
            UserDTO reactivatedUser = adminDAO.reactivateUser(userDTO);

            // Send reactivation email with new password
            String fullName = userToReactivate.getFName() + " " +
                    (userToReactivate.getLName() != null ? userToReactivate.getLName() : "");
            emailService.sendReactivationEmail(
                    userToReactivate.getEmail(),
                    fullName.trim(),
                    newTemporaryPassword
            );

            return reactivatedUser;

        } catch (Exception e) {
            throw new RuntimeException("Failed to reactivate user: " + e.getMessage());
        }
    }
}