package com.UniHUB.Server.controller;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.dto.CourseDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.dto.FeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/student")
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
}
