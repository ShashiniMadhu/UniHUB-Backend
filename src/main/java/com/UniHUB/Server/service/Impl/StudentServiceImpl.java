package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;
import com.UniHUB.Server.dto.PassPaperDTO;
import com.UniHUB.Server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class StudentServiceImpl implements StudentService {


    @Autowired
    private StudentDAO studentDAO;

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

    @Override
    public Map<Integer, List<PassPaperDTO>> getAllPassPapersGroupedByYear() {
        return studentDAO.getAllPassPapersGroupedByYear();
    }


}
