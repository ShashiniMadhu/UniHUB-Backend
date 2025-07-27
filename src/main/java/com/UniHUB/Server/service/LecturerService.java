package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.AnnouncementDTO;

public interface LecturerService {
    AnnouncementDTO publishAnnouncement(AnnouncementDTO announcementDTO);
}