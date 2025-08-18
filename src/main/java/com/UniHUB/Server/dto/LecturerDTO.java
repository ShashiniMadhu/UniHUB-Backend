package com.UniHUB.Server.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDTO {
    @JsonProperty("lecturer_id")
    private int lecturerId;
    @JsonProperty("user_id")
    private int userId;
    private UserDTO userDTO;
}
