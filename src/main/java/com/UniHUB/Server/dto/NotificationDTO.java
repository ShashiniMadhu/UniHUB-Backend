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

public class NotificationDTO {

    @JsonProperty("notification_id")
    private Integer notificationId;
    @JsonProperty("user_id")
    private Integer userId;
    private String message;
    @JsonProperty("is_read")
    private Boolean isRead;
    @JsonProperty("is_delete")
    private Boolean isDelete;
}
