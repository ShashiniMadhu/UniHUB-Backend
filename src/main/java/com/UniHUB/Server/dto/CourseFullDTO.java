package com.UniHUB.Server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseFullDTO {

    @JsonProperty("course_id")
    private int courseId;

    private String name;

    private int credits;

    private int year;

    private int semester;

    @JsonProperty("lecturer_id")
    private Integer lecturerId;
}
