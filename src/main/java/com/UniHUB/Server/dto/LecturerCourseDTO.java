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

public class LecturerCourseDTO {
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    @JsonProperty("course_id")
    private Integer courseId;
}
