package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.QueryDTO;
import com.UniHUB.Server.dto.CourseDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.dto.FeedbackDTO;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {
    @Autowired
    private DatabaseConnection databaseConnection;

    // Implementation for student queries
    @Override
    public void addQuery(QueryDTO query) {
        String sql = "INSERT INTO query (course_id, student_id, category, priority, question) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, query.getCourseId());
            stmt.setInt(2, query.getStudentId());
            stmt.setString(3, query.getCategory());
            stmt.setString(4, query.getPriority());
            stmt.setString(5, query.getQuestion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    rs.getInt("query_id"),
                    rs.getInt("course_id"),
                    rs.getInt("student_id"),
                    rs.getString("category"),
                    rs.getString("priority"),
                    rs.getString("question")
                );
                queries.add(query);
            }
        } catch (SQLException e) {
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
                    rs.getInt("lecturer_id"),
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
        String sql = "INSERT INTO feedback (student_id, course_id, lecturer_id, review, rate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getStudentId());
            stmt.setInt(2, feedback.getCourseId());
            stmt.setInt(3, feedback.getLecturerId());
            stmt.setString(4, feedback.getReview());
            stmt.setInt(5, feedback.getRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // QueryDAO is deprecated and should be removed. No references to QueryDAO should remain in the codebase.
}
