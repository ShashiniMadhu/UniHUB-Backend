package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AssignmentsDTO;
import com.UniHUB.Server.dto.ResourceDTO;

public interface LecturerService {
    AnnouncementDTO publishAnnouncement(AnnouncementDTO announcementDTO);
    AssignmentsDTO publishAssignment(AssignmentsDTO assignmentsDTO);
    ResourceDTO publishresources(ResourceDTO resourceDTO);
}