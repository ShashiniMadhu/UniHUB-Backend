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

public class ResourceDTO {

    @JsonProperty("resource_id")
    private Integer resourceId;
    @JsonProperty("lecturer_id")
    private Integer lecturerId;
    @JsonProperty("course_id")
    private Integer courseId;
    @JsonProperty("file_name")
    private String fileName;
    private String attachment;
}
