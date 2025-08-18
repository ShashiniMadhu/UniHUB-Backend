package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByLecturerId(Integer lecturerId);
}
