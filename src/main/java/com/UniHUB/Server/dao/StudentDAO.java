package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;
import com.UniHUB.Server.dto.QueryDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import com.UniHUB.Server.dto.CourseDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.dto.FeedbackDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

// DAO Interface
public interface StudentDAO {
    QueryDTO addQuery(QueryDTO query);
    List<QueryDTO> getQueriesByStudentId(int studentId);

    AppointmentDTO makeAppointment(AppointmentDTO appointmentDTO);

    public boolean isLecturerAvailable(Integer lecturerId, LocalDate date, LocalTime time);

    List<AppointmentDTO> getAllAppointmentsByStudentId(Integer studentId);
    AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO);
    String deleteAppointment(Integer appointmentId);

    List<LecturerDTO> getAllLecturers();
    List<QueryDTO> getQueriesByStudentIdAndCourseId(int studentId, int courseId);
    List<CourseDTO> getCoursesByStudentId(int studentId);
    List<ResourceDTO> getResourcesByCourseId(int courseId);
    void addResource(ResourceDTO resource);
    List<FeedbackDTO> getFeedbackByCourseId(int courseId);
    void addFeedback(FeedbackDTO feedback);
    boolean updateQuery(QueryDTO query) throws SQLException;
    Timestamp getQueryCreatedTime(int queryId) throws SQLException;
}

// Only the StudentDAO interface remains here. Implementation is now in dao/Impl/StudentDAOImpl.java
