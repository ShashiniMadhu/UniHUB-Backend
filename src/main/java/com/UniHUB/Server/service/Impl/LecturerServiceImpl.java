package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dto.*;
import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerDAO lecturerDAO;

    @Override
    public AnnouncementDTO publishAnnouncement(AnnouncementDTO announcementDTO) {
        return lecturerDAO.saveAnnouncement(announcementDTO);
    }

    @Override
    public AssignmentsDTO publishAssignment(AssignmentsDTO assignmentsDTO){
        return lecturerDAO.saveAssignments(assignmentsDTO);
    }

    @Override
    public ResourceDTO publishresources(ResourceDTO resourceDTO){
        return lecturerDAO.saveResources(resourceDTO);
    }

    @Override
    public List<FeedbackDTO> getFeedbackForLecturer(Integer lecturerId) {
        return lecturerDAO.findFeedbackByLecturer(lecturerId);
    }

    @Override
    public List<AnnouncementDTO> getAnnouncementsByLecturerId(Integer lecturerId) {
        // 1. Fetch courses for this lecturer
        List<LecturerCourseDTO> courses = lecturerDAO.findCoursesByLecturerId(lecturerId);

        // 2. For each course, fetch and collect announcements
        List<AnnouncementDTO> announcements = new ArrayList<>();
        for (LecturerCourseDTO course : courses) {
            announcements.addAll(
                    lecturerDAO.findAnnouncementsByCourseId(course.getCourseId())
            );
        }
        return announcements;
    }

    @Override
    public AnnouncementDTO updateAnnouncement(AnnouncementDTO dto) {
        return lecturerDAO.updateAnnouncement(dto);
    }

    @Override
    public void deleteAnnouncement(Integer announcementId) {
        boolean ok = lecturerDAO.deleteAnnouncement(announcementId);
        if (!ok) {
            throw new RuntimeException("Announcement not found with id " + announcementId);
        }
    }

    @Override
    public List<AssignmentsDTO> getAssignmentsByLecturer(Integer lecturerId) {
        List<LecturerCourseDTO> courses = lecturerDAO.findCoursesByLecturerId(lecturerId);

        List<AssignmentsDTO> allAssignments = new ArrayList<>();
        for (LecturerCourseDTO course : courses) {
            List<AssignmentsDTO> assignmentsForCourse =
                    lecturerDAO.findAssignmentsByCourse(course.getCourseId());
            allAssignments.addAll(assignmentsForCourse);
        }

        return allAssignments;
    }

    @Override
    public void deleteAssignment(Integer assignmentId) {
        boolean ok = lecturerDAO.deleteAssignment(assignmentId);
        if (!ok) {
            throw new RuntimeException("Announcement not found with id " + assignmentId);
        }
    }


    @Override
    public List<ResourceDTO> getResources(Integer lecturerId) {
        List<LecturerCourseDTO> courses =
                lecturerDAO.findCoursesByLecturerId(lecturerId);
        List<ResourceDTO> all = new ArrayList<>();
        for (LecturerCourseDTO c : courses) {
            all.addAll(lecturerDAO.findResourcesByCourse(c.getCourseId()));
        }
        return all;
    }

    @Override
    public void deleteResource(Integer resourceId) {
        boolean ok = lecturerDAO.deleteResource(resourceId);
        if (!ok) {
            throw new RuntimeException("Resource not found with id " + resourceId);
        }
    }

    @Override
    public List<AppointmentDTO> getPendingAppointments(Integer lecturerId) {
        return lecturerDAO.findPendingAppointmentsByLecturerId(lecturerId);
    }

    @Override
    public AppointmentDTO takeAppointment(Integer lecturerId, Integer appointmentId) {
        return lecturerDAO.takeAppointment(lecturerId, appointmentId);
    }

    @Override
    public AppointmentDTO rejectAppointment(Integer lecturerId, Integer appointmentId) {
        return lecturerDAO.rejectAppointment(lecturerId, appointmentId);
    }

    @Override
    public List<SiteAnnouncementDTO> getAllSiteAnnouncements() {
        return lecturerDAO.findAllSiteAnnouncements();
    }







}
