package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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
    Map<Integer, List<PassPaperDTO>> getAllPassPapersGroupedByYear();

}

// Only the StudentDAO interface remains here. Implementation is now in dao/Impl/StudentDAOImpl.java
