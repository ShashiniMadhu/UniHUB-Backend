package com.UniHUB.Server.controller;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.dto.CourseDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/student")
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
}
