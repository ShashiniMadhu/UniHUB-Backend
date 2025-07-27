package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AssignmentsDTO;
import com.UniHUB.Server.dto.ResourceDTO;

public interface LecturerDAO {
    AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO);
    AssignmentsDTO saveAssignments(AssignmentsDTO assignmentsDTO);
    ResourceDTO saveResources(ResourceDTO resourceDTO);

}