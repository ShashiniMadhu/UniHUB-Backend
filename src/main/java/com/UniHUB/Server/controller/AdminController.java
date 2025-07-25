package com.UniHUB.Server.controller;

import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping(value = "create_user")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            final UserDTO user = adminService.createUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
