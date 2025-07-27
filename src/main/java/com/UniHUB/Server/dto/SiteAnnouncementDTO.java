package com.UniHUB.Server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteAnnouncementDTO {

    @JsonProperty("announcement_id")
    private Integer announcementId;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("description")
    private String description;

    @JsonProperty("attachments")
    private String attachments;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty("time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    @JsonProperty("type")
    private String type; // "teacher" or "student"

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Additional constructor for creating new announcements (without ID and created_at)
    public SiteAnnouncementDTO(String topic, String description, String attachments,
                               LocalDate date, LocalTime time, String type) {
        this.topic = topic;
        this.description = description;
        this.attachments = attachments;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    // Constructor for response without sensitive timestamp info if needed
    public SiteAnnouncementDTO(Integer announcementId, String topic, String description,
                               String attachments, LocalDate date, LocalTime time, String type) {
        this.announcementId = announcementId;
        this.topic = topic;
        this.description = description;
        this.attachments = attachments;
        this.date = date;
        this.time = time;
        this.type = type;
    }
}