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
    @JsonProperty("user_Id")
    private Integer userId;

    @JsonProperty("f_name")
    private String fName;

    @JsonProperty("l_name")
    private String lName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("NIC")
    private String nic;

    @JsonProperty("address")
    private String address;

    @JsonProperty("contact")
    private String contact;

    @JsonProperty("DOB")
    private LocalDate dob;

    @JsonProperty("role")
    private String role; // STUDENT, ADMIN, LECTURER

    @JsonProperty("password")
    private String password;

    @JsonProperty("status")
    private String status; // ACTIVE, DEACTIVATED

    // Additional fields for specific roles
    @JsonProperty("student_Id")
    private Integer studentId;

    @JsonProperty("lecturer_Id")
    private Integer lecturerId;
}