package com.UniHUB.Server.controller;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;
import com.UniHUB.Server.dto.QueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    private final StudentDAO studentDAO;

    @Autowired
    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @PostMapping(value="/submit")
    public void submitQuery(@RequestBody QueryDTO query) {
        studentDAO.addQuery(query);
    }

    @GetMapping("/{studentId}")
    public List<QueryDTO> getStudentQueries(@PathVariable int studentId) {
        return studentDAO.getQueriesByStudentId(studentId);
    }

    @GetMapping("lecturers")
    public List<LecturerDTO> getAllLecturers() {
        return studentDAO.getAllLecturers();
    }

    @GetMapping("/appointments/{studentId}")
    public List<AppointmentDTO> getAllAppointmentsByStudentId(@PathVariable int studentId) {
        return studentDAO.getAllAppointmentsByStudentId(studentId);
    }

    @PostMapping("/appointment")
    public ResponseEntity<?> makeAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try {
            AppointmentDTO createdAppointment = studentDAO.makeAppointment(appointmentDTO);
            return ResponseEntity.ok(createdAppointment);
        } catch (RuntimeException e) {
            // For example, lecturer not available
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating appointment");
        }
    }

    // ---------------- Check Lecturer Availability ----------------
    @GetMapping("/appointment/check")
    public ResponseEntity<?> isLecturerAvailable(
            @RequestParam Integer lecturerId,
            @RequestParam String date,
            @RequestParam String time
    ) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            LocalTime localTime = LocalTime.parse(time);
            boolean available = studentDAO.isLecturerAvailable(lecturerId, localDate, localTime);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date/time format");
        }
    }

    @PutMapping("/appointment")
    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO updated = studentDAO.updateAppointment(appointmentDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/appointments/{id}")
    public String deleteAppointment(@PathVariable Integer id) {
        return studentDAO.deleteAppointment(id);
    }


}

