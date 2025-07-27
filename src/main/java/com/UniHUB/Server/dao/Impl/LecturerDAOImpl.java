package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dto.AnnouncementDTO;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
