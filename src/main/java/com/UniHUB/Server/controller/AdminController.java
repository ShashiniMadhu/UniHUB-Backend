package com.UniHUB.Server.controller;

import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}