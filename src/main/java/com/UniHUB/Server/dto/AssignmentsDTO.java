package com.UniHUB.Server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssignmentsDTO {

    @JsonProperty("assignment_id")
    private Integer assignmentId;
    @JsonProperty("course_id")
    private Integer courseId;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;

    private String title;
    private String description;
    private String attachment;

    private LocalDate date;

}
