package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AssignmentsDTO;

public interface LecturerDAO {
    AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO);
    AssignmentsDTO saveAssignments(AssignmentsDTO assignmentsDTO);

}