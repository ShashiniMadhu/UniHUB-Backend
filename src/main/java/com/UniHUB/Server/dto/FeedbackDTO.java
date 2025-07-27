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

public class FeedbackDTO {

    @JsonProperty("feedback_id")
    private Integer feedbackId;
    @JsonProperty("student_id")
    private Integer studentId;
    @JsonProperty("course_id")
    private Integer courseId;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;

    private String review;
    private Integer rate;
}
