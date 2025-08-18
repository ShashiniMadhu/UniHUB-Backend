package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.AppointmentDTO;
import com.UniHUB.Server.dto.LecturerDTO;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.dto.CourseDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {
    @Autowired
    private DatabaseConnection databaseConnection;

    // Implementation for student queries
    @Override
    public QueryDTO addQuery(QueryDTO query) {
        String sql = "INSERT INTO query (course_Id, student_Id, category, priority, question) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, query.getCourseId());
            stmt.setInt(2, query.getStudentId());
            stmt.setString(3, query.getCategory());
            stmt.setString(4, query.getPriority());
            stmt.setString(5, query.getQuestion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Better error handling - log the error and potentially re-throw
            System.err.println("Error inserting query: " + e.getMessage());
            e.printStackTrace();
            // Consider throwing a custom exception or handling appropriately
            // throw new DatabaseException("Failed to add query", e);
        }
        return query;
    }

    @Override
    public List<QueryDTO> getQueriesByStudentId(int studentId) {
        List<QueryDTO> queries = new ArrayList<>();
        String sql = "SELECT * FROM query WHERE student_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QueryDTO query = new QueryDTO(
                    rs.getInt("query_Id"),
                    rs.getInt("course_Id"),
                    rs.getInt("student_Id"),
                    rs.getString("category"),
                    rs.getString("Priority"),
                    rs.getString("question"),
                        rs.getTimestamp("created_at")
                );
                queries.add(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queries;
    }

    // ---------------- Appointments ----------------
    @Override
    public boolean isLecturerAvailable(Integer lecturerId, LocalDate date, LocalTime time) {
        String sql = "SELECT COUNT(*) FROM appointment WHERE lecturer_Id = ? AND date = ? AND time = ? AND status='APPROVED'";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(time));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // true if no conflict
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // assume not available if error occurs
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByStudentId(Integer studentId) {
        List<AppointmentDTO> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE student_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AppointmentDTO appointment = new AppointmentDTO(
                        rs.getInt("appointment_id"),
                        rs.getInt("lecturer_id"),
                        rs.getInt("student_id"),
                        rs.getString("purpose"),
                        rs.getDate("date").toLocalDate(),       // Convert to LocalDate
                        rs.getTime("time").toLocalTime(),       // Convert to LocalTime
                        rs.getString("status"),                 // You missed status in your example
                        rs.getString("location"),
                        rs.getInt("duration")
                );

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        String sql = """
        UPDATE appointment
        SET lecturer_id=?, purpose=?, date=?, time=?, duration=?
        WHERE status='PENDING' AND appointment_id=? AND student_id=?
        """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentDTO.getLecturerId());
            ps.setString(2, appointmentDTO.getPurpose());
            ps.setDate(3, Date.valueOf(appointmentDTO.getDate()));
            ps.setTime(4, Time.valueOf(appointmentDTO.getTime()));
            ps.setInt(5, appointmentDTO.getDuration());
            ps.setInt(6, appointmentDTO.getAppointmentId());

            ps.setInt(7, appointmentDTO.getStudentId()); // assuming you have this in DTO


            if (!"APPROVED".equals(appointmentDTO.getStatus()) && !"REJECTED".equals(appointmentDTO.getStatus())) {
                throw new IllegalStateException("Only APPROVED or REJECTED appointments can be updated");
            }

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No appointments found with id" +
                        appointmentDTO.getAppointmentId());
            }
            return appointmentDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error setting appointment", e);
        }

    }

    @Override
    public String deleteAppointment(Integer appointmentId) {
        String checkSql = "SELECT status FROM appointment WHERE appointment_id = ?";
        String deleteSql = "DELETE FROM appointment WHERE appointment_id = ?";

        try (Connection conn = databaseConnection.getConnection()) {

            // 1️⃣ Check appointment status
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, appointmentId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (!rs.next()) {
                        return "Appointment not found with id " + appointmentId;
                    }

                    String status = rs.getString("status");
                    if (!"PENDING".equalsIgnoreCase(status)) {
                        return "Cannot delete appointment. Current status is '" + status + "'";
                    }
                }
            }

            // 2️⃣ Delete if status is PENDING
            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.setInt(1, appointmentId);
                deletePs.executeUpdate();
            }

            return "Appointment successfully deleted";

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    @Override
    public List<LecturerDTO> getAllLecturers() {
        List<LecturerDTO> lecturers = new ArrayList<>();
        String sql = "SELECT l.lecturer_id, u.user_id, u.f_name, u.l_name, u.email " +
                "FROM lecturer l " +
                "INNER JOIN user u ON l.user_id = u.user_id";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LecturerDTO lecturer = new LecturerDTO();
                lecturer.setLecturerId(rs.getInt("lecturer_id"));
                lecturer.setUserId(rs.getInt("user_id"));

                UserDTO user = new UserDTO();
                user.setUserId(rs.getInt("user_id"));
                user.setFName(rs.getString("f_name") + " " + rs.getString("l_name"));
                user.setEmail(rs.getString("email"));


                lecturer.setUserDTO(user);

                lecturers.add(lecturer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching lecturers", e);
        }

        return lecturers;
    }



    @Override
    public AppointmentDTO makeAppointment(AppointmentDTO appointmentDTO) {
        if (!isLecturerAvailable(appointmentDTO.getLecturerId(), appointmentDTO.getDate(), appointmentDTO.getTime())) {
            throw new RuntimeException("Lecturer is not available at this time.");
        }

        String sql = "INSERT INTO appointment (lecturer_Id, student_Id, purpose, date, time, status,location,duration) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, appointmentDTO.getLecturerId());
            stmt.setInt(2, appointmentDTO.getStudentId());
            stmt.setString(3, appointmentDTO.getPurpose());
            stmt.setDate(4, Date.valueOf(appointmentDTO.getDate()));
            stmt.setTime(5, Time.valueOf(appointmentDTO.getTime()));
            stmt.setString(6, appointmentDTO.getStatus());
            stmt.setString(7,appointmentDTO.getLocation());
            stmt.setInt(8,appointmentDTO.getDuration());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointmentDTO.setAppointmentId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointmentDTO;
    }
    @Override
    public List<QueryDTO> getQueriesByStudentIdAndCourseId(int studentId, int courseId) {
        List<QueryDTO> queries = new ArrayList<>();
        String sql = "SELECT * FROM query WHERE student_Id = ? AND course_Id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                QueryDTO query = new QueryDTO();
                query.setQueryId(rs.getInt("query_Id"));
                query.setCourseId(rs.getInt("course_Id"));
                query.setStudentId(rs.getInt("student_Id"));
                query.setCategory(rs.getString("category"));
                query.setPriority(rs.getString("Priority"));
                query.setQuestion(rs.getString("question"));
                query.setCreatedAt(rs.getTimestamp("created_at"));
                queries.add(query);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching queries by student and course: " + e.getMessage());
            e.printStackTrace();
        }
        return queries;
    }

    @Override
    public List<CourseDTO> getCoursesByStudentId(int studentId) {
        List<CourseDTO> courses = new ArrayList<>();
        String sql = "SELECT c.course_Id, c.name FROM course c JOIN student_course sc ON c.course_Id = sc.course_Id WHERE sc.student_Id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(new CourseDTO(rs.getInt("course_Id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<ResourceDTO> getResourcesByCourseId(int courseId) {
        List<ResourceDTO> resources = new ArrayList<>();
        String sql = "SELECT * FROM resource WHERE course_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resources.add(new ResourceDTO(
                    rs.getInt("resource_id"),
                    rs.getInt("lecturer_id"),
                    rs.getInt("course_id"),
                    rs.getString("file_name"),
                    rs.getString("attachment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    @Override
    public void addResource(ResourceDTO resource) {
        String sql = "INSERT INTO resource (lecturer_id, course_id, file_name, attachment) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resource.getLecturerId());
            stmt.setInt(2, resource.getCourseId());
            stmt.setString(3, resource.getFileName());
            stmt.setString(4, resource.getAttachment());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FeedbackDTO> getFeedbackByCourseId(int courseId) {
        List<FeedbackDTO> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE course_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                feedbackList.add(new FeedbackDTO(
                    rs.getInt("feedback_id"),
                    rs.getInt("student_id"),
                    rs.getInt("course_id"),

                    rs.getString("review"),
                    rs.getInt("rate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    @Override
    public void addFeedback(FeedbackDTO feedback) {
        String sql = "INSERT INTO feedback (student_Id, course_Id, review, rate) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getStudentId());
            stmt.setInt(2, feedback.getCourseId());

            stmt.setString(3, feedback.getReview());
            stmt.setInt(4, feedback.getRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateQuery(QueryDTO query) throws SQLException {
        String sql = "UPDATE query SET category = ?, priority = ?, question = ? WHERE query_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, query.getCategory());
            stmt.setString(2, query.getPriority());
            stmt.setString(3, query.getQuestion());
            stmt.setInt(4, query.getQueryId());

            int updated = stmt.executeUpdate();
            return updated > 0;
        }
    }

    @Override
    public Timestamp getQueryCreatedTime(int queryId) throws SQLException {
        String sql = "SELECT created_at FROM query WHERE query_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, queryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("created_at");
            } else {
                return null;
            }
        }
    }

    // QueryDAO is deprecated and should be removed. No references to QueryDAO should remain in the codebase.
}
