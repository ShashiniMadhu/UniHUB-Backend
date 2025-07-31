package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.StudentDAO;
import com.UniHUB.Server.dto.QueryDTO;
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

    // QueryDAO is deprecated and should be removed. No references to QueryDAO should remain in the codebase.
}
