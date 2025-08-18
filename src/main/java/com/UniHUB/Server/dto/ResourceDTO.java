package com.UniHUB.Server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    // Additional fields for enhanced functionality
    @JsonProperty("lecturer_name")
    private String lecturerName;
    @JsonProperty("course_name")
    private String courseName;
    @JsonProperty("file_size")
    private String fileSize;
    @JsonProperty("upload_date")
    private LocalDateTime uploadDate;
    @JsonProperty("download_count")
    private Integer downloadCount;
    @JsonProperty("file_type")
    private String fileType;
    private String description;

    public ResourceDTO(int resourceId, int lecturerId, int courseId, String fileName, String attachment) {
    }
}
