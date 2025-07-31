package com.UniHUB.Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private int userId;
    private String fName;
    private String email;
    private String role;
    private String token;

}
