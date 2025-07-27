package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AssignmentsDTO;
import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.dto.ResourceDTO;

import java.util.List;

public interface LecturerService {
    AnnouncementDTO publishAnnouncement(AnnouncementDTO announcementDTO);
    AssignmentsDTO publishAssignment(AssignmentsDTO assignmentsDTO);
    ResourceDTO publishresources(ResourceDTO resourceDTO);
    List<FeedbackDTO> getFeedbackForLecturer(Integer lecturerId);
    List<AnnouncementDTO> getAnnouncementsByLecturerId(Integer lecturerId);
    AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO);
    void deleteAnnouncement(Integer announcementId);


}