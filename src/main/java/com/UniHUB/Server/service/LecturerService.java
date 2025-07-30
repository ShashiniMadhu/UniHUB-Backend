package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.*;

import java.util.List;

public interface LecturerService {
    AnnouncementDTO publishAnnouncement(AnnouncementDTO announcementDTO);
    AssignmentsDTO publishAssignment(AssignmentsDTO assignmentsDTO);
    ResourceDTO publishresources(ResourceDTO resourceDTO);
    List<FeedbackDTO> getFeedbackForLecturer(Integer lecturerId);
    List<AnnouncementDTO> getAnnouncementsByLecturerId(Integer lecturerId);
    AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO);
    void deleteAnnouncement(Integer announcementId);
    List<AssignmentsDTO> getAssignmentsByLecturer(Integer lecturerId);
    void deleteAssignment(Integer assignmentId);
    List<ResourceDTO> getResources(Integer lecturerId);
    void deleteResource(Integer resourceId);
    List<AppointmentDTO> getPendingAppointments(Integer lecturerId);
    AppointmentDTO takeAppointment(Integer lecturerId, Integer appointmentId);
    AppointmentDTO rejectAppointment(Integer lecturerId, Integer appointmentId);
    List<SiteAnnouncementDTO> getAllSiteAnnouncements();

}