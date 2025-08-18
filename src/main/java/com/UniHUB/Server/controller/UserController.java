package com.UniHUB.Server.controller;

import com.UniHUB.Server.dto.ForgotPasswordRequest;
import com.UniHUB.Server.dto.ResetPasswordRequest;
import com.UniHUB.Server.dto.UserLoginDTO;
import com.UniHUB.Server.dto.UserResponseDTO;
import com.UniHUB.Server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.generateResetToken(request);
        return ResponseEntity.ok("Password reset link sent (check console or email)");
    }



    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful");
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO loginDTO) {
        UserResponseDTO response = userService.login(loginDTO);
        System.out.println("Email: " + loginDTO.getEmail());


        return ResponseEntity.ok(response);
    }
}
