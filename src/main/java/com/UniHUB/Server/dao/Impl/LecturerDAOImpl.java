package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.LecturerDAO;
import com.UniHUB.Server.dto.*;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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



    @Override
    public List<AppointmentDTO> findPendingAppointmentsByLecturerId(Integer lecturerId) {
        String sql = "SELECT appointment_id, student_id, purpose, date, time, status "
                + "FROM appointment "
                + "WHERE lecturer_id = ? AND status = 'PENDING'";
        List<AppointmentDTO> result = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lecturerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // convert SQL types to DTO types
                    LocalDate date  = rs.getDate("date").toLocalDate();
                    LocalTime time  = rs.getTime("time").toLocalTime();

                    // use setters (or builder) since no matching all-args constructor exists
                    AppointmentDTO dto = new AppointmentDTO();
                    dto.setAppointmentId(rs.getInt("appointment_id"));
                    dto.setStudentId(rs.getInt("student_id"));
                    dto.setPurpose(rs.getString("purpose"));
                    dto.setDate(date);
                    dto.setTime(time);
                    dto.setStatus(rs.getString("status"));

                    result.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pending appointments", e);
        }

        return result;
    }

    @Override
    public AppointmentDTO takeAppointment(Integer lecturerId, Integer appointmentId) {
        String sql =
                "UPDATE appointment " +
                        "SET status = ? " +
                        "WHERE  appointment_id = ? AND lecturer_id = ? AND status = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "APPROVED");
            ps.setInt(2, appointmentId);
            ps.setInt(3, lecturerId);
            ps.setString(4, "PENDING");

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Invalid or non-pending appointment");
            }

            AppointmentDTO dto = new AppointmentDTO();
            dto.setAppointmentId(appointmentId);
            dto.setStatus("TAKEN");
            return dto;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment status", e);
        }
    }

    @Override
    public AppointmentDTO rejectAppointment(Integer lecturerId, Integer appointmentId) {
        String sql =
                "UPDATE appointment " +
                        "SET status = ? " +
                        "WHERE appointment_id = ? AND lecturer_id = ? AND status = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "REJECTED");
            ps.setInt(2, appointmentId);
            ps.setInt(3, lecturerId);
            ps.setString(4, "PENDING");

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Invalid or non-pending appointment");
            }

            AppointmentDTO dto = new AppointmentDTO();
            dto.setAppointmentId(appointmentId);
            dto.setStatus("REJECTED");
            return dto;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment status", e);
        }
    }

    @Override
    public List<SiteAnnouncementDTO> findAllSiteAnnouncements() {
        List<SiteAnnouncementDTO> list = new ArrayList<>();
        String sql = "SELECT announcement_id, topic, description,date,time,created_at FROM site_announcements ORDER BY created_at DESC";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SiteAnnouncementDTO dto = new SiteAnnouncementDTO();
                dto.setAnnouncementId(rs.getInt("announcement_id"));
                dto.setTopic(rs.getString("topic"));
                dto.setDescription(rs.getString("description"));
                dto.setDate(rs.getDate("date").toLocalDate());
                dto.setTime(rs.getTime("time").toLocalTime());
                dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching site announcements", e);
        }
        return list;
    }

    @Override
    public List<LecturerQueryDTO> findQueriesByLecturerId(Integer lecturerId) {
        String sql = """
            SELECT 
                q.query_id,
                q.lecturer_id,
                q.student_id,
                q.question
            FROM query q
            WHERE q.lecturer_id = ?
            ORDER BY q.query_id DESC
            """;

        List<LecturerQueryDTO> list = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LecturerQueryDTO dto = new LecturerQueryDTO(
                            rs.getInt("query_id"),
                            rs.getInt("lecturer_id"),
                            rs.getInt("student_id"),
                            rs.getString("question")
                    );
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching queries for lecturer", e);
        }
        return list;
    }

    @Override
    public QueryReplyDTO saveQueryReply(QueryReplyDTO queryReplyDTO) {
        String sql = """
            INSERT INTO query_reply (query_id, reply)
            VALUES (?, ?)
        """;

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, queryReplyDTO.getQueryId());
            ps.setString(2, queryReplyDTO.getReply());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating query reply failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    queryReplyDTO.setReplyId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating query reply failed, no ID obtained.");
                }
            }

            return queryReplyDTO;

        } catch (SQLException e) {
            throw new RuntimeException("Database error while saving query reply: " + e.getMessage(), e);
        }
    }

    @Override
    public AnnouncementDTO editAnnouncement(AnnouncementDTO announcementDTO, boolean preserveExistingAttachment) {
        String sql;

        if (preserveExistingAttachment) {
            sql = "UPDATE announcement SET content = ?, link = ? WHERE announcement_id = ?";
        } else {
            sql = "UPDATE announcement SET content = ?, link = ?, attachment = ? WHERE announcement_id = ?";
        }

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, announcementDTO.getContent());
            statement.setString(2, announcementDTO.getLink());

            if (preserveExistingAttachment) {
                statement.setInt(3, announcementDTO.getAnnouncementId());
            } else {
                statement.setString(3, announcementDTO.getAttachment());
                statement.setInt(4, announcementDTO.getAnnouncementId());
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Fetch and return the updated announcement
                return findAnnouncementById(announcementDTO.getAnnouncementId());
            } else {
                throw new RuntimeException("Announcement not found with id: " + announcementDTO.getAnnouncementId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating announcement: " + e.getMessage(), e);
        }
    }

    private AnnouncementDTO findAnnouncementById(Integer announcementId) {
        String sql = "SELECT announcement_id, lecturer_id, course_id, content, link, attachment FROM announcement WHERE announcement_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, announcementId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AnnouncementDTO dto = new AnnouncementDTO();
                dto.setAnnouncementId(resultSet.getInt("announcement_id"));
                dto.setLecturerId(resultSet.getInt("lecturer_id"));
                dto.setCourseId(resultSet.getInt("course_id"));
                dto.setContent(resultSet.getString("content"));
                dto.setLink(resultSet.getString("link"));
                dto.setAttachment(resultSet.getString("attachment"));
                return dto;
            } else {
                throw new RuntimeException("Announcement not found with id: " + announcementId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching announcement: " + e.getMessage(), e);
        }
    }

    @Override
    public AssignmentsDTO editAssignment(AssignmentsDTO assignmentsDTO, boolean preserveExistingAttachment) {
        String sql;

        if (preserveExistingAttachment) {
            // Update without changing attachment
            sql = "UPDATE assignments SET title = ?, description = ?, date = ? WHERE assignment_id = ?";
        } else {
            // Update including attachment
            sql = "UPDATE assignments SET title = ?, description = ?, date = ?, attachment = ? WHERE assignment_id = ?";
        }

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, assignmentsDTO.getTitle());
            statement.setString(2, assignmentsDTO.getDescription());
            statement.setDate(3, Date.valueOf(assignmentsDTO.getDate()));

            if (preserveExistingAttachment) {
                statement.setInt(4, assignmentsDTO.getAssignmentId());
            } else {
                statement.setString(4, assignmentsDTO.getAttachment());
                statement.setInt(5, assignmentsDTO.getAssignmentId());
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Fetch and return the updated assignment
                return findAssignmentById(assignmentsDTO.getAssignmentId());
            } else {
                throw new RuntimeException("Assignment not found with id: " + assignmentsDTO.getAssignmentId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating assignment: " + e.getMessage(), e);
        }
    }

    private AssignmentsDTO findAssignmentById(Integer assignmentId) {
        String sql = "SELECT assignment_id, course_id, lecturer_id, title, description, attachment, date FROM assignments WHERE assignment_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, assignmentId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                AssignmentsDTO dto = new AssignmentsDTO();
                dto.setAssignmentId(resultSet.getInt("assignment_id"));
                dto.setCourseId(resultSet.getInt("course_id"));
                dto.setLecturerId(resultSet.getInt("lecturer_id"));
                dto.setTitle(resultSet.getString("title"));
                dto.setDescription(resultSet.getString("description"));
                dto.setAttachment(resultSet.getString("attachment"));

                Date sqlDate = resultSet.getDate("date");
                if (sqlDate != null) {
                    dto.setDate(sqlDate.toLocalDate());
                }

                return dto;
            } else {
                throw new RuntimeException("Assignment not found with id: " + assignmentId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching assignment: " + e.getMessage(), e);
        }
    }

    @Override
    public ResourceDTO editResource(ResourceDTO resourceDTO, boolean preserveExistingAttachment) {
        String sql;

        if (preserveExistingAttachment) {
            // Update without changing attachment
            sql = "UPDATE resource SET file_name = ? WHERE resource_id = ?";
        } else {
            // Update including attachment
            sql = "UPDATE resource SET file_name = ?, attachment = ? WHERE resource_id = ?";
        }

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, resourceDTO.getFileName());

            if (preserveExistingAttachment) {
                statement.setInt(2, resourceDTO.getResourceId());
            } else {
                statement.setString(2, resourceDTO.getAttachment());
                statement.setInt(3, resourceDTO.getResourceId());
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Fetch and return the updated resource
                return findResourceById(resourceDTO.getResourceId());
            } else {
                throw new RuntimeException("Resource not found with id: " + resourceDTO.getResourceId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating resource: " + e.getMessage(), e);
        }
    }

    private ResourceDTO findResourceById(Integer resourceId) {
        String sql = "SELECT resource_id, lecturer_id, course_id, file_name, attachment FROM resource WHERE resource_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, resourceId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ResourceDTO dto = new ResourceDTO();
                dto.setResourceId(resultSet.getInt("resource_id"));
                dto.setLecturerId(resultSet.getInt("lecturer_id"));
                dto.setCourseId(resultSet.getInt("course_id"));
                dto.setFileName(resultSet.getString("file_name"));
                dto.setAttachment(resultSet.getString("attachment"));
                return dto;
            } else {
                throw new RuntimeException("Resource not found with id: " + resourceId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching resource: " + e.getMessage(), e);
        }
    }

    @Override
    public Integer findUserIdByLecturerId(Integer lecturerId) {
        String sql = "SELECT user_id FROM lecturer WHERE lecturer_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, lecturerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user ID for lecturer ID: " + lecturerId, e);
        }
    }

    @Override
    public UserDTO findUserDetailsById(Integer userId) {
        String sql = "SELECT user_id, f_name, l_name, email, NIC, address, contact, DOB, role, password, status FROM user WHERE user_id = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    UserDTO user = new UserDTO();
                    user.setUserId(resultSet.getInt("user_id"));
                    user.setFName(resultSet.getString("f_name"));
                    user.setLName(resultSet.getString("l_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setNic(resultSet.getString("NIC"));
                    user.setAddress(resultSet.getString("address"));
                    user.setContact(resultSet.getString("contact"));

                    Date dobDate = resultSet.getDate("DOB");
                    if (dobDate != null) {
                        user.setDob(dobDate.toLocalDate());
                    }

                    user.setRole(resultSet.getString("role"));
                    user.setPassword(resultSet.getString("password"));
                    user.setStatus(resultSet.getString("status"));

                    return user;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user details for user ID: " + userId, e);
        }
    }
    @Override
    public List<NotificationDTO> findNotificationsByUserId(Integer userId) {
        List<NotificationDTO> notifications = new ArrayList<>();
        String sql = "SELECT notification_id, user_id, message, is_read, is_delete " +
                "FROM notification WHERE user_id = ? AND is_delete = FALSE " +
                "ORDER BY notification_id DESC";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    NotificationDTO notification = new NotificationDTO();
                    notification.setNotificationId(resultSet.getInt("notification_id"));
                    notification.setUserId(resultSet.getInt("user_id"));
                    notification.setMessage(resultSet.getString("message"));
                    notification.setIsRead(resultSet.getBoolean("is_read"));
                    notification.setIsDelete(resultSet.getBoolean("is_delete"));

                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching notifications for user ID: " + userId, e);
        }

        return notifications;
    }







    @Override
    public List<AppointmentDTO> getAllAppointmentsByLecturerId(Integer lecturerId) {
        List<AppointmentDTO> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE student_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
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
    public AppointmentDTO setAppointment( AppointmentDTO appointmentDTO) {
        String sql = "UPDATE appointment SET location = ?, status= ?WHERE studentId = ? AND appointmentId=?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, appointmentDTO.getLocation());
            ps.setString(2, appointmentDTO.getStatus());
            ps.setInt(3, appointmentDTO.getStudentId());
            ps.setInt(4, appointmentDTO.getAppointmentId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No appointments found with appointment_id and student_id " +
                        appointmentDTO.getAppointmentId()+appointmentDTO.getStudentId());
            }
            return appointmentDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error setting appointment", e);
        }
    }


}