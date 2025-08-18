package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.*;

import java.util.List;

public interface LecturerDAO {
    AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO);
    AssignmentsDTO saveAssignments(AssignmentsDTO assignmentsDTO);
    ResourceDTO saveResources(ResourceDTO resourceDTO);
    List<FeedbackDTO> findFeedbackByLecturer(Integer lecturerId);
    List<LecturerCourseDTO> findCoursesByLecturerId(Integer lecturerId);
    List<AnnouncementDTO> findAnnouncementsByCourseId(Integer courseId);
    AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO);
    boolean deleteAnnouncement(Integer announcementId);
    List<AssignmentsDTO> findAssignmentsByCourse(Integer courseId);
    boolean deleteAssignment(Integer assignmentId);
    List<ResourceDTO> findResourcesByCourse(Integer courseId);
    boolean deleteResource(Integer resourceId);
    List<AppointmentDTO> findPendingAppointmentsByLecturerId(Integer lecturerId);
    AppointmentDTO takeAppointment(Integer lecturerId, Integer appointmentId);
    AppointmentDTO rejectAppointment(Integer lecturerId, Integer appointmentId);
    List<NotificationDTO> findByUserId(Integer userId);
    List<SiteAnnouncementDTO> findAllSiteAnnouncements();
    List<LecturerQueryDTO> findQueriesByLecturerId(Integer lecturerId);
    QueryReplyDTO saveQueryReply(QueryReplyDTO queryReplyDTO);

    List<AppointmentDTO> getAllAppointmentsByLecturerId(Integer lecturerId);
    AppointmentDTO setAppointment(AppointmentDTO appointmentDTO);




}