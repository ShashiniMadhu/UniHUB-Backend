package com.UniHUB.Server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AnnouncementDTO {

    @JsonProperty("announcement_id")
    private Integer announcementId;

    @JsonProperty("lecturer_id")
    private Integer lecturerId;

    @JsonProperty("course_id")
    private Integer courseId;


    private String content;

    private String link;

    private String attachment;
}
