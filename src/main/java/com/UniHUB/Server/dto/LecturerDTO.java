package com.UniHUB.Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDTO {

    private int lecturerId;
    private int userId;
    private UserDTO userDTO;
}
