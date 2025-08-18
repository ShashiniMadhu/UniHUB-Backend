package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;
import com.UniHUB.Server.dto.PassPaperDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface StudentService {

    AppointmentDTO makeAppointment(AppointmentDTO appointmentDTO);
    public boolean isLecturerAvailable(Integer lecturerId, LocalDate date, LocalTime time);


    List<AppointmentDTO> getAllAppointmentsByStudentId(Integer studentId);
    AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO);
    String deleteAppointment(Integer appointmentId);
    List<LecturerDTO> getAllLecturers();
    Map<Integer, List<PassPaperDTO>> getAllPassPapersGroupedByYear();

}
