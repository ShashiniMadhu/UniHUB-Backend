package com.UniHUB.Server.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LecturerQueryDTO {

     @JsonProperty("query_id")
     private int queryId;
     @JsonProperty("lecturer_id")
     private int lecturerId;
     @JsonProperty("student_id")
     private int studentId;

     private String question;
}
