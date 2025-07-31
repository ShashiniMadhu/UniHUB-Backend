package com.UniHUB.Server.controller;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.QueryDTO;
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
}

