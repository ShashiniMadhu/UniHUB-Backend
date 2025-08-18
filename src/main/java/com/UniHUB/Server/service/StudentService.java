package com.UniHUB.Server.service;


import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;
import com.UniHUB.Server.dto.ResourceDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface StudentService {

    AppointmentDTO makeAppointment(AppointmentDTO appointmentDTO);
    public boolean isLecturerAvailable(Integer lecturerId, LocalDate date, LocalTime time);


    void addFeedback(FeedbackDTO feedback);
    boolean updateQuery(QueryDTO queryDTO);


    List<AppointmentDTO> getAllAppointmentsByStudentId(Integer studentId);
    AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO);
    String deleteAppointment(Integer appointmentId);
    List<LecturerDTO> getAllLecturers();

    // Resource management methods
    List<ResourceDTO> getResourcesByStudentId(int studentId);
    ResourceDTO getResourceById(int resourceId);
    byte[] downloadResource(int resourceId);
}
