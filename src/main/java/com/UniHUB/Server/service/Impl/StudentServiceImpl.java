package com.UniHUB.Server.service.Impl;


import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;

import com.UniHUB.Server.dto.ResourceDTO;

import com.UniHUB.Server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.dto.QueryDTO;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class StudentServiceImpl implements StudentService {



    @Autowired
    private StudentDAO studentDAO;



    @Override
    public void addFeedback(FeedbackDTO feedback) {

            // You can add validation or business logic here before saving
            if (feedback.getRate() < 1 || feedback.getRate() > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }


        studentDAO.addFeedback(feedback);



    }

    @Override
    public boolean updateQuery(QueryDTO queryDTO) {

        try {

            Timestamp createdAt = studentDAO.getQueryCreatedTime(queryDTO.getQueryId());
            if (createdAt == null) {
                throw new IllegalArgumentException("Query not found");
            }

            // Check 2-minutes limit
            Instant twoMinutesLater = createdAt.toInstant().plusSeconds(120);
            if (Instant.now().isAfter(twoMinutesLater)) {
                throw new IllegalArgumentException("Query can only be updated within 2 minutes of creation");
            }

            return studentDAO.updateQuery(queryDTO);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public AppointmentDTO makeAppointment(AppointmentDTO appointmentDTO) {
        return studentDAO.makeAppointment(appointmentDTO);
    }

    @Override
    public boolean isLecturerAvailable(Integer lecturerId, LocalDate date, LocalTime time) {
        return studentDAO.isLecturerAvailable(lecturerId, date, time);
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByStudentId(Integer studentId) {
        return studentDAO.getAllAppointmentsByStudentId(studentId);
    }

    @Override
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        return studentDAO.updateAppointment(appointmentDTO);
    }

    @Override
    public String deleteAppointment(Integer appointmentId) {
        return studentDAO.deleteAppointment(appointmentId);
    }

    @Override
    public List<LecturerDTO> getAllLecturers() {
        return studentDAO.getAllLecturers();
    }


    // Resource management methods implementation
    @Override
    public List<ResourceDTO> getResourcesByStudentId(int studentId) {
        return studentDAO.getResourcesByStudentId(studentId);
    }

    @Override
    public ResourceDTO getResourceById(int resourceId) {
        return studentDAO.getResourceById(resourceId);
    }

    @Override
    public byte[] downloadResource(int resourceId) {
        return studentDAO.downloadResource(resourceId);
    }
}
