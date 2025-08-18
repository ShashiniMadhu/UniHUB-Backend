package com.UniHUB.Server.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppointmentDTO {

    @JsonProperty("appointment_id")
    private Integer appointmentId;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    @JsonProperty("student_id")
    private Integer studentId;

    private String  purpose;
    private LocalDate date;
    private LocalTime time;
    private String   status;
    private String location;
    private Integer duration;

}
