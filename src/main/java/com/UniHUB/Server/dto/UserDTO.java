package com.UniHUB.Server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("fName")
    private String fName;

    @JsonProperty("lName")
    private String lName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("nic")
    private String nic;

    @JsonProperty("address")
    private String address;

    @JsonProperty("contact")
    private String contact;

    @JsonProperty("dob")
    private LocalDate dob;

    @JsonProperty("role")
    private String role; // STUDENT, ADMIN, LECTURER

    @JsonProperty("password")
    private String password;

    // Additional fields for specific roles
    @JsonProperty("studentId")
    private Integer studentId; // Only populated for STUDENT role

    @JsonProperty("lecturerId")
    private Integer lecturerId; // Only populated for LECTURER role
}