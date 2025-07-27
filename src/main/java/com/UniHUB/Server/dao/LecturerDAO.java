package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.AnnouncementDTO;

public interface LecturerDAO {
    AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO);
}