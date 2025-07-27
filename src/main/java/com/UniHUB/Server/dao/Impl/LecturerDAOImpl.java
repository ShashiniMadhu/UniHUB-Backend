package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.dto.AssignmentsDTO;
import com.UniHUB.Server.dto.ResourceDTO;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;

@Repository
public class LecturerDAOImpl implements LecturerDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public AnnouncementDTO saveAnnouncement(AnnouncementDTO announcementDTO) {
        String sql = """
            INSERT INTO announcement (lecturer_id, course_id, content, link, attachment)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setInt(1, announcementDTO.getLecturerId());
            statement.setInt(2, announcementDTO.getCourseId());
            statement.setString(3, announcementDTO.getContent());
            statement.setString(4, announcementDTO.getLink());
            statement.setString(5, announcementDTO.getAttachment());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating announcement failed, no rows affected.");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    announcementDTO.setAnnouncementId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating announcement failed, no ID obtained.");
                }
            }

            return announcementDTO;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database error while saving announcement: " + e.getMessage(), e
            );
        }
    }


    @Override
    public AssignmentsDTO saveAssignments(AssignmentsDTO assignmentsDTO) {
        String sql = """
            INSERT INTO assignments (lecturer_id, course_id, title, description, attachment , date)
            VALUES (?, ?, ?, ?, ?,?)
        """;

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS
                )
        ) {
            ps.setInt(1, assignmentsDTO.getLecturerId());
            ps.setInt(2, assignmentsDTO.getCourseId());
            ps.setString(3, assignmentsDTO.getTitle());
            ps.setString(4, assignmentsDTO.getDescription());
            ps.setString(5, assignmentsDTO.getAttachment());
            ps.setDate(6, Date.valueOf(assignmentsDTO.getDate() != null ? assignmentsDTO.getDate() : LocalDate.now()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating announcement failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    assignmentsDTO.setAssignmentId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating announcement failed, no ID obtained.");
                }
            }

            return assignmentsDTO;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database error while saving announcement: " + e.getMessage(), e
            );
        }
    }

    @Override
    public ResourceDTO saveResources(ResourceDTO resourceDTO){
        String sql = """
            INSERT INTO resource (lecturer_id, course_id, file_name, attachment)
            VALUES (?, ?, ?, ?)
        """;
        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS
                )
        ){
            ps.setInt(1,resourceDTO.getLecturerId());
            ps.setInt(2,resourceDTO.getCourseId());
            ps.setString(3,resourceDTO.getFileName());
            ps.setString(4,resourceDTO.getAttachment());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating announcement failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    resourceDTO.setResourceId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating announcement failed, no ID obtained.");
                }
            }

            return resourceDTO;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database error while saving announcement: " + e.getMessage(), e
            );
        }
    }

}