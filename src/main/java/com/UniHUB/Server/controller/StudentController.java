package com.UniHUB.Server.controller;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    private final StudentDAO studentDAO;

    @Autowired
    public StudentController(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @PostMapping(value="/{studentId}/submit")
    public QueryDTO submitQuery(@RequestBody QueryDTO queryDTO) {
        final QueryDTO query = studentDAO.addQuery(queryDTO);
        return query;
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



    @GetMapping("/{studentId}/courses")
    public List<CourseDTO> getStudentCourses(@PathVariable int studentId) {
        return studentDAO.getCoursesByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}/resources")
    public List<ResourceDTO> getCourseResources(@PathVariable int courseId) {
        return studentDAO.getResourcesByCourseId(courseId);
    }

    @PostMapping("/course/{courseId}/resources")
    public void addCourseResource(@PathVariable int courseId, @RequestBody ResourceDTO resource) {
        resource.setCourseId(courseId);
        studentDAO.addResource(resource);
    }

    @GetMapping("/course/{courseId}/feedback")
    public List<FeedbackDTO> getCourseFeedback(@PathVariable int courseId) {
        return studentDAO.getFeedbackByCourseId(courseId);
    }

    @PostMapping("/course/{courseId}/feedback")
    public void addCourseFeedback(@PathVariable int courseId, @RequestBody FeedbackDTO feedback) {
        feedback.setCourseId(courseId);
        studentDAO.addFeedback(feedback);
    }

    @GetMapping("/{studentId}/queries")
    public List<QueryDTO> getStudentQueriesByCourse(
            @PathVariable int studentId,
            @RequestParam(value = "course_Id", required = false) Integer courseId) {
        if (courseId != null) {
            return studentDAO.getQueriesByStudentIdAndCourseId(studentId, courseId);
        } else {
            return studentDAO.getQueriesByStudentId(studentId);
        }
    }

    @GetMapping("/pass-papers")
    public Map<Integer, List<PassPaperDTO>> getAllPassPapersGroupedByYear() {
        return studentDAO.getAllPassPapersGroupedByYear();
    }

}

