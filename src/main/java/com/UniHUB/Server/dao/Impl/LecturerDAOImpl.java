package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dto.*;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<FeedbackDTO> findFeedbackByLecturer(Integer lecturerId) {
        String sql = """
            SELECT f.feedback_id,
                   f.student_id,
                   f.course_id,
                   f.lecturer_id,
                   f.review,
                   f.rate
              FROM feedback f
              JOIN lecturer_course lc
                ON f.course_id = lc.course_id
               AND f.lecturer_id = lc.lecturer_id
             WHERE lc.lecturer_id = ?
            """;

        List<FeedbackDTO> list = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FeedbackDTO dto = new FeedbackDTO(
                            rs.getInt("feedback_id"),
                            rs.getInt("student_id"),
                            rs.getInt("course_id"),
                            rs.getInt("lecturer_id"),
                            rs.getString("review"),
                            rs.getInt("rate")
                    );
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching feedback", e);
        }
        return list;
    }

    @Override
    public List<LecturerCourseDTO> findCoursesByLecturerId(Integer lecturerId) {
        String sql = """
            SELECT 
              lc.course_id    AS courseId,
              lc.lecturer_id  AS lecturerId
            FROM lecturer_course lc
            WHERE lc.lecturer_id = ?
            """;

        List<LecturerCourseDTO> list = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LecturerCourseDTO dto = new LecturerCourseDTO();
                    dto.setCourseId(rs.getInt("courseId"));
                    dto.setLecturerId(rs.getInt("lecturerId"));
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses", e);
        }
        return list;
    }

    @Override
    public List<AnnouncementDTO> findAnnouncementsByCourseId(Integer courseId) {
        String sql = """
            SELECT
              a.announcement_id AS announcementId,
              a.course_id       AS courseId,
              a.link           AS link,
              a.content         AS content,
              a.attachment      AS attachment
            FROM announcement a
            WHERE a.course_id = ?
            ORDER BY a.announcement_id DESC
            """;

        List<AnnouncementDTO> list = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AnnouncementDTO dto = new AnnouncementDTO();
                    dto.setAnnouncementId(rs.getInt("announcementId"));
                    dto.setCourseId(rs.getInt("courseId"));
                    dto.setLink(rs.getString("link"));
                    dto.setContent(rs.getString("content"));
                    dto.setAttachment(rs.getString("attachment"));
                    // no createdAt column any more
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching announcements", e);
        }
        return list;
    }

    @Override
    public AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO) {
        String sql = """
            UPDATE announcement
               SET content    = ?,
                   link       = ?,
                   attachment = ?
             WHERE announcement_id = ?
            """;
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, announcementDTO.getContent());
            ps.setString(2, announcementDTO.getLink());
            ps.setString(3, announcementDTO.getAttachment());
            ps.setInt(4, announcementDTO.getAnnouncementId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No announcement found with id " +
                        announcementDTO.getAnnouncementId());
            }
            return announcementDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating announcement", e);
        }
    }



    @Override
    public boolean deleteAnnouncement(Integer announcementId) {
        String sql = "DELETE FROM announcement WHERE announcement_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, announcementId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting announcement", e);
        }
    }

    @Override
    public List<AssignmentsDTO> findAssignmentsByCourse(Integer courseId) {
        String sql = """
        SELECT
          a.assignment_id   AS assignmentId,
          a.course_id       AS courseId,
          a.lecturer_id     AS lecturerId,
          a.title           AS title,
          a.description     AS description,
          a.attachment      AS attachment,
          a.date            AS date
        FROM assignments a
        WHERE a.course_id = ?
        """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                List<AssignmentsDTO> list = new ArrayList<>();
                while (rs.next()) {
                    // read raw date string
                    String rawDate = rs.getString("date");
                    LocalDate date = null;
                    if (rawDate != null && !"0000-00-00".equals(rawDate)) {
                        date = LocalDate.parse(rawDate);
                    }
                    AssignmentsDTO dto = new AssignmentsDTO(
                            rs.getInt("assignmentId"),
                            rs.getInt("courseId"),
                            rs.getInt("lecturerId"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("attachment"),
                            date
                    );
                    list.add(dto);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching assignments", e);
        }
    }

    @Override
    public boolean deleteAssignment(Integer assignmentId) {
        String sql = "DELETE FROM assignments WHERE assignment_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting assignment", e);
        }
    }

    @Override
    public List<ResourceDTO> findResourcesByCourse(Integer courseId) {
        String sql = "SELECT resource_id, course_id, file_name, attachment "
                + "FROM resource WHERE course_id = ?";
        List<ResourceDTO> list = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ResourceDTO r = new ResourceDTO();
                    r.setResourceId(rs.getInt("resource_id"));
                    r.setCourseId(rs.getInt("course_id"));
                    r.setFileName(rs.getString("file_name"));
                    r.setAttachment(rs.getString("attachment"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching resources for course " + courseId, e);
        }
        return list;
    }

    @Override
    public boolean deleteResource(Integer resourceId) {
        String sql = "DELETE FROM resource WHERE resource_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resourceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting resource", e);
        }
    }







}