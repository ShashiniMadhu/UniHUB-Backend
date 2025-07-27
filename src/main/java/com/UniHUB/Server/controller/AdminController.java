package com.UniHUB.Server.controller;

import com.UniHUB.Server.dto.SiteAnnouncementDTO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"}) // Updated CORS
@RestController
@RequestMapping("/admin") // Updated to match your frontend API call
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping(value = "/create_user") // Added leading slash
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            final UserDTO user = adminService.createUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/view_students") // Added leading slash
    public ResponseEntity<?> viewStudents(){
        try{
            final List<UserDTO> students = adminService.viewStudents();
            return ResponseEntity.ok(students);
        }catch(Exception e){
            return new ResponseEntity<>("Error view students: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/view_lecturers") // Added leading slash
    public ResponseEntity<?> viewLecturers(){
        try{
            final List<UserDTO> lecturers = adminService.viewLecturers();
            return ResponseEntity.ok(lecturers);
        }catch(Exception e){
            return new ResponseEntity<>("Error view lecturers: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/create_announcements")
    public ResponseEntity<?> createSiteAnnouncement(@RequestBody SiteAnnouncementDTO siteAnnouncementDTO){
        try{
            if (siteAnnouncementDTO.getTopic() == null || siteAnnouncementDTO.getTopic().trim().isEmpty()) {
                return new ResponseEntity<>("Topic is required and cannot be empty", HttpStatus.BAD_REQUEST);
            }

            final SiteAnnouncementDTO announcement = adminService.createSiteAnnouncement(siteAnnouncementDTO);
            return ResponseEntity.ok(announcement);
        }catch (Exception e){
            e.printStackTrace(); // Add stack trace for debugging
            return new ResponseEntity<>("Error creating announcement: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/view_announcements")
    public ResponseEntity<?> viewAnnouncements(){
        try{
            final List<SiteAnnouncementDTO> announcements = adminService.viewAnnouncements();
            return  ResponseEntity.ok(announcements);
        }catch (Exception e){
            return new ResponseEntity<>("Error fetching announcement: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete_announcement")
    public ResponseEntity<?> deleteAnnouncement(@RequestBody SiteAnnouncementDTO siteAnnouncementDTO) {
        try {
            if (siteAnnouncementDTO.getAnnouncementId() == null) {
                return new ResponseEntity<>("Announcement ID is required", HttpStatus.BAD_REQUEST);
            }

            final SiteAnnouncementDTO announcement = adminService.deleteAnnouncement(siteAnnouncementDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "Announcement deleted successfully",
                    "deletedAnnouncement", announcement
            ));

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>("Announcement not found with ID: " + siteAnnouncementDTO.getAnnouncementId(),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Error deleting announcement: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update_announcement")
    public ResponseEntity<?> updateAnnouncement(@RequestBody SiteAnnouncementDTO siteAnnouncementDTO) {
        try {
            if (siteAnnouncementDTO.getAnnouncementId() == null) {
                return new ResponseEntity<>("Announcement ID is required", HttpStatus.BAD_REQUEST);
            }

            if (siteAnnouncementDTO.getTopic() == null || siteAnnouncementDTO.getTopic().trim().isEmpty()) {
                return new ResponseEntity<>("Topic is required and cannot be empty", HttpStatus.BAD_REQUEST);
            }

            final SiteAnnouncementDTO announcement = adminService.updateAnnouncement(siteAnnouncementDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "Announcement updated successfully",
                    "updatedAnnouncement", announcement
            ));

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>("Announcement not found with ID: " + siteAnnouncementDTO.getAnnouncementId(),
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Error updating announcement: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/deactivate_user")
    public ResponseEntity<?> deactivateUser(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getUserId() == null) {
                return new ResponseEntity<>("User ID is required", HttpStatus.BAD_REQUEST);
            }

            final UserDTO deactivatedUser = adminService.deactivateUser(userDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "User deactivated successfully",
                    "deactivatedUser", deactivatedUser
            ));

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>("User not found with ID: " + userDTO.getUserId(),
                        HttpStatus.NOT_FOUND);
            }
            if (e.getMessage().contains("already deactivated")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Error deactivating user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/reactivate_user")
    public ResponseEntity<?> reactivateUser(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getUserId() == null) {
                return new ResponseEntity<>("User ID is required", HttpStatus.BAD_REQUEST);
            }

            final UserDTO reactivatedUser = adminService.reactivateUser(userDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "User reactivated successfully",
                    "reactivatedUser", reactivatedUser
            ));

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>("User not found with ID: " + userDTO.getUserId(),
                        HttpStatus.NOT_FOUND);
            }
            if (e.getMessage().contains("not deactivated")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Error reactivating user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}