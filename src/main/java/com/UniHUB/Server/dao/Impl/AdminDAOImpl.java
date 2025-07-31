package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.AdminDAO;
import com.UniHUB.Server.dto.SiteAnnouncementDTO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO {

    @Autowired
    private DatabaseConnection databaseConnection;

    // Helper method to close resources
    private void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Connection connection = null;
        PreparedStatement userStatement = null;
        PreparedStatement roleStatement = null;

        try {
            connection = databaseConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // First, insert into user table
            String userSql = """
                    INSERT INTO user (f_name, l_name, email, NIC, address, contact, DOB, role, password)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            userStatement = connection.prepareStatement(userSql, PreparedStatement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, userDTO.getFName());
            userStatement.setString(2, userDTO.getLName());
            userStatement.setString(3, userDTO.getEmail());
            userStatement.setString(4, userDTO.getNic());
            userStatement.setString(5, userDTO.getAddress());
            userStatement.setString(6, userDTO.getContact());
            userStatement.setDate(7, Date.valueOf(userDTO.getDob()));
            userStatement.setString(8, userDTO.getRole());
            userStatement.setString(9, userDTO.getPassword());

            int userRowsAffected = userStatement.executeUpdate();

            if (userRowsAffected > 0) {
                // Get the generated user_id
                try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        userDTO.setUserId(userId);

                        // Based on role, insert into appropriate table
                        if ("STUDENT".equals(userDTO.getRole())) {
                            String studentSql = """
                                    INSERT INTO student (user_Id)
                                    VALUES (?)
                                    """;
                            roleStatement = connection.prepareStatement(studentSql, PreparedStatement.RETURN_GENERATED_KEYS);
                            roleStatement.setInt(1, userId);
                            roleStatement.executeUpdate();

                            // Get the generated student_id
                            try (ResultSet studentKeys = roleStatement.getGeneratedKeys()) {
                                if (studentKeys.next()) {
                                    userDTO.setStudentId(studentKeys.getInt(1));
                                }
                            }

                        } else if ("LECTURER".equals(userDTO.getRole())) {
                            String lecturerSql = """
                                    INSERT INTO lecturer (user_Id)
                                    VALUES (?)
                                    """;
                            roleStatement = connection.prepareStatement(lecturerSql, PreparedStatement.RETURN_GENERATED_KEYS);
                            roleStatement.setInt(1, userId);
                            roleStatement.executeUpdate();

                            // Get the generated lecturer_id
                            try (ResultSet lecturerKeys = roleStatement.getGeneratedKeys()) {
                                if (lecturerKeys.next()) {
                                    userDTO.setLecturerId(lecturerKeys.getInt(1));
                                }
                            }
                        }
                        // For ADMIN role, we don't need to insert into any other table

                        connection.commit(); // Commit transaction
                        return userDTO;
                    }
                }
            }

            connection.rollback(); // Rollback if something went wrong
            throw new RuntimeException("Failed to create user");

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Database error while creating user: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            closeResources(roleStatement, userStatement, connection);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<UserDTO> users = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            String sql = """
            SELECT user_id, f_name, l_name, email, NIC, address, 
                   contact, DOB, role, password, status
            FROM user
            """;

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(resultSet.getInt("user_id"));

                // Concatenate first name and last name as full name
                String fullName = resultSet.getString("f_name") + " " + resultSet.getString("l_name");
                userDTO.setFName(fullName);  // Using fName field for full name
                userDTO.setLName(null);      // Null because full name is stored in fName

                userDTO.setEmail(resultSet.getString("email"));
                userDTO.setNic(resultSet.getString("NIC"));
                userDTO.setAddress(resultSet.getString("address"));
                userDTO.setContact(resultSet.getString("contact"));
                userDTO.setDob(resultSet.getDate("DOB").toLocalDate());
                userDTO.setRole(resultSet.getString("role"));
                userDTO.setPassword(null); // Do not expose password
                userDTO.setStatus(resultSet.getString("status"));

                // These fields are null unless joined with student/lecturer table
                userDTO.setStudentId(null);
                userDTO.setLecturerId(null);

                users.add(userDTO);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving users: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return users;
    }


    @Override
    public List<UserDTO> viewStudents() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<UserDTO> students = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            String sql = """
                SELECT u.user_id, u.f_name, u.l_name, u.email, u.NIC, u.address, 
                       u.contact, u.DOB, u.role,u.password,u.status, s.student_id
                FROM user u
                INNER JOIN student s ON u.user_id = s.user_Id
                WHERE u.role = 'STUDENT'
                """;

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(resultSet.getInt("user_id"));

                // Concatenate first name and last name as full name
                String fullName = resultSet.getString("f_name") + " " + resultSet.getString("l_name");
                userDTO.setFName(fullName); // Store full name in fName field
                userDTO.setLName(null); // Set lName to null since we're using fName for full name

                userDTO.setEmail(resultSet.getString("email"));
                userDTO.setNic(resultSet.getString("NIC"));
                userDTO.setAddress(resultSet.getString("address"));
                userDTO.setContact(resultSet.getString("contact"));
                userDTO.setDob(resultSet.getDate("DOB").toLocalDate());
                userDTO.setRole(resultSet.getString("role"));
                userDTO.setPassword(resultSet.getString("password"));
                userDTO.setStatus(resultSet.getString("status"));
                userDTO.setStudentId(resultSet.getInt("student_id"));
                userDTO.setPassword(null); // Don't return password for security

                students.add(userDTO);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving students: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return students;
    }

    @Override
    public List<UserDTO> viewLecturers() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<UserDTO> lecturers = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            String sql = """
                SELECT u.user_id, u.f_name, u.l_name, u.email, u.NIC, u.address, 
                       u.contact, u.DOB, u.role,u.password,u.status, l.lecturer_id
                FROM user u
                INNER JOIN lecturer l ON u.user_id = l.user_Id
                WHERE u.role = 'LECTURER'
                """;

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(resultSet.getInt("user_id"));

                // Concatenate first name and last name as full name
                String fullName = resultSet.getString("f_name") + " " + resultSet.getString("l_name");
                userDTO.setFName(fullName); // Store full name in fName field
                userDTO.setLName(null); // Set lName to null since we're using fName for full name

                userDTO.setEmail(resultSet.getString("email"));
                userDTO.setNic(resultSet.getString("NIC"));
                userDTO.setAddress(resultSet.getString("address"));
                userDTO.setContact(resultSet.getString("contact"));
                userDTO.setDob(resultSet.getDate("DOB").toLocalDate());
                userDTO.setRole(resultSet.getString("role"));
                userDTO.setPassword(resultSet.getString("password"));
                userDTO.setStatus(resultSet.getString("status"));
                userDTO.setLecturerId(resultSet.getInt("lecturer_id"));
                userDTO.setPassword(null); // Don't return password for security

                lecturers.add(userDTO);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving lecturers: " + e.getMessage());
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return lecturers;
    }

    @Override
    public SiteAnnouncementDTO createSiteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = databaseConnection.getConnection();

            String sql = """
                INSERT INTO site_announcements (topic, description, attachments, Date, time, type)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, siteAnnouncementDTO.getTopic());
            statement.setString(2, siteAnnouncementDTO.getDescription());
            statement.setString(3, siteAnnouncementDTO.getAttachments());

            // Handle LocalDate to SQL Date conversion
            if (siteAnnouncementDTO.getDate() != null) {
                statement.setDate(4, Date.valueOf(siteAnnouncementDTO.getDate()));
            } else {
                statement.setNull(4, java.sql.Types.DATE);
            }

            // Handle LocalTime to SQL Time conversion
            if (siteAnnouncementDTO.getTime() != null) {
                statement.setTime(5, java.sql.Time.valueOf(siteAnnouncementDTO.getTime()));
            } else {
                statement.setNull(5, java.sql.Types.TIME);
            }

            // Set the type (teacher or student)
            statement.setString(6, siteAnnouncementDTO.getType() != null ? siteAnnouncementDTO.getType() : "student");

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Get the generated announcement_id
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int announcementId = generatedKeys.getInt(1);
                        siteAnnouncementDTO.setAnnouncementId(announcementId);

                        // Set created_at to current timestamp (will be set by database)
                        // We can fetch it back if needed, or let the database handle it

                        return siteAnnouncementDTO;
                    }
                }
            }

            throw new RuntimeException("Failed to create site announcement - no rows affected");

        } catch (SQLException e) {
            throw new RuntimeException("Database error while creating site announcement: " + e.getMessage(), e);
        } finally {
            closeResources(statement, connection);
        }
    }

    @Override
    public List<SiteAnnouncementDTO> viewAnnouncements() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<SiteAnnouncementDTO> announcements = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            String sql = """
            SELECT announcement_id, topic, description, attachments, Date, time, type, created_at
            FROM site_announcements
            ORDER BY created_at DESC
            """;

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                SiteAnnouncementDTO announcementDTO = new SiteAnnouncementDTO();

                announcementDTO.setAnnouncementId(resultSet.getInt("announcement_id"));
                announcementDTO.setTopic(resultSet.getString("topic"));
                announcementDTO.setDescription(resultSet.getString("description"));
                announcementDTO.setAttachments(resultSet.getString("attachments"));

                // Handle Date conversion (can be null)
                java.sql.Date sqlDate = resultSet.getDate("Date");
                if (sqlDate != null) {
                    announcementDTO.setDate(sqlDate.toLocalDate());
                }

                // Handle Time conversion (can be null)
                java.sql.Time sqlTime = resultSet.getTime("time");
                if (sqlTime != null) {
                    announcementDTO.setTime(sqlTime.toLocalTime());
                }

                announcementDTO.setType(resultSet.getString("type"));

                // Handle created_at timestamp
                java.sql.Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
                if (createdAtTimestamp != null) {
                    announcementDTO.setCreatedAt(createdAtTimestamp.toLocalDateTime());
                }

                announcements.add(announcementDTO);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving announcements: " + e.getMessage(), e);
        } finally {
            closeResources(resultSet, statement, connection);
        }

        return announcements;
    }

    @Override
    public SiteAnnouncementDTO deleteAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            // First check if announcement exists
            String selectSql = "SELECT * FROM site_announcements WHERE announcement_id = ?";
            selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, siteAnnouncementDTO.getAnnouncementId());
            resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Announcement not found with ID: " + siteAnnouncementDTO.getAnnouncementId());
            }

            // Store the announcement data before deletion
            SiteAnnouncementDTO existingAnnouncement = new SiteAnnouncementDTO();
            existingAnnouncement.setAnnouncementId(resultSet.getInt("announcement_id"));
            existingAnnouncement.setTopic(resultSet.getString("topic"));
            existingAnnouncement.setDescription(resultSet.getString("description"));
            existingAnnouncement.setAttachments(resultSet.getString("attachments"));

            java.sql.Date sqlDate = resultSet.getDate("Date");
            if (sqlDate != null) {
                existingAnnouncement.setDate(sqlDate.toLocalDate());
            }

            java.sql.Time sqlTime = resultSet.getTime("time");
            if (sqlTime != null) {
                existingAnnouncement.setTime(sqlTime.toLocalTime());
            }

            existingAnnouncement.setType(resultSet.getString("type"));

            java.sql.Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
            if (createdAtTimestamp != null) {
                existingAnnouncement.setCreatedAt(createdAtTimestamp.toLocalDateTime());
            }

            // Now delete the announcement
            String deleteSql = "DELETE FROM site_announcements WHERE announcement_id = ?";
            deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, siteAnnouncementDTO.getAnnouncementId());

            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                return existingAnnouncement; // Return the deleted announcement data
            } else {
                throw new RuntimeException("Failed to delete announcement with ID: " + siteAnnouncementDTO.getAnnouncementId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting announcement: " + e.getMessage(), e);
        } finally {
            closeResources(resultSet, selectStatement, deleteStatement, connection);
        }
    }

    @Override
    public SiteAnnouncementDTO updateAnnouncement(SiteAnnouncementDTO siteAnnouncementDTO) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            // First check if announcement exists
            String selectSql = "SELECT announcement_id FROM site_announcements WHERE announcement_id = ?";
            selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, siteAnnouncementDTO.getAnnouncementId());
            resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Announcement not found with ID: " + siteAnnouncementDTO.getAnnouncementId());
            }

            // Now update the announcement
            String updateSql = """
            UPDATE site_announcements 
            SET topic = ?, description = ?, attachments = ?, Date = ?, time = ?, type = ?
            WHERE announcement_id = ?
            """;

            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, siteAnnouncementDTO.getTopic());
            updateStatement.setString(2, siteAnnouncementDTO.getDescription());
            updateStatement.setString(3, siteAnnouncementDTO.getAttachments());

            // Handle LocalDate to SQL Date conversion
            if (siteAnnouncementDTO.getDate() != null) {
                updateStatement.setDate(4, Date.valueOf(siteAnnouncementDTO.getDate()));
            } else {
                updateStatement.setNull(4, java.sql.Types.DATE);
            }

            // Handle LocalTime to SQL Time conversion
            if (siteAnnouncementDTO.getTime() != null) {
                updateStatement.setTime(5, java.sql.Time.valueOf(siteAnnouncementDTO.getTime()));
            } else {
                updateStatement.setNull(5, java.sql.Types.TIME);
            }

            updateStatement.setString(6, siteAnnouncementDTO.getType());
            updateStatement.setInt(7, siteAnnouncementDTO.getAnnouncementId());

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                return siteAnnouncementDTO;
            } else {
                throw new RuntimeException("Failed to update announcement with ID: " + siteAnnouncementDTO.getAnnouncementId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating announcement: " + e.getMessage(), e);
        } finally {
            closeResources(resultSet, selectStatement, updateStatement, connection);
        }
    }

    @Override
    public UserDTO deactivateUser(UserDTO userDTO) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            // First check if user exists
            String selectSql = "SELECT user_id, f_name, l_name, email, status FROM user WHERE user_id = ?";
            selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, userDTO.getUserId());
            resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("User not found with ID: " + userDTO.getUserId());
            }

            // Check if user is already deactivated
            String currentStatus = resultSet.getString("status");
            if ("DEACTIVATED".equals(currentStatus)) {
                throw new RuntimeException("User is already deactivated");
            }

            // Store user info for return
            UserDTO existingUser = new UserDTO();
            existingUser.setUserId(resultSet.getInt("user_id"));
            existingUser.setFName(resultSet.getString("f_name"));
            existingUser.setLName(resultSet.getString("l_name"));
            existingUser.setEmail(resultSet.getString("email"));

            // Generate a random deactivation token as password (user won't be able to login)
            String deactivationToken = "DEACTIVATED_" + System.currentTimeMillis();
            String hashedDeactivationToken = new BCryptPasswordEncoder().encode(deactivationToken);

            // Update user status and password
            String updateSql = "UPDATE user SET status = 'DEACTIVATED', password = ? WHERE user_id = ?";
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, hashedDeactivationToken);
            updateStatement.setInt(2, userDTO.getUserId());

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                existingUser.setStatus("DEACTIVATED");
                existingUser.setPassword(null); // Don't return password
                return existingUser;
            } else {
                throw new RuntimeException("Failed to deactivate user with ID: " + userDTO.getUserId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while deactivating user: " + e.getMessage(), e);
        } finally {
            closeResources(resultSet, selectStatement, updateStatement, connection);
        }
    }

    @Override
    public UserDTO reactivateUser(UserDTO userDTO) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            // First check if user exists
            String selectSql = "SELECT user_id, f_name, l_name, email, status FROM user WHERE user_id = ?";
            selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, userDTO.getUserId());
            resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("User not found with ID: " + userDTO.getUserId());
            }

            // Check if user is actually deactivated
            String currentStatus = resultSet.getString("status");
            if (!"DEACTIVATED".equals(currentStatus)) {
                throw new RuntimeException("User is not deactivated. Current status: " + currentStatus);
            }

            // Store user info for return
            UserDTO existingUser = new UserDTO();
            existingUser.setUserId(resultSet.getInt("user_id"));
            existingUser.setFName(resultSet.getString("f_name"));
            existingUser.setLName(resultSet.getString("l_name"));
            existingUser.setEmail(resultSet.getString("email"));

            // The new password will be set in the service layer
            // Update user status and password with the provided hashed password
            String updateSql = "UPDATE user SET status = 'ACTIVE', password = ? WHERE user_id = ?";
            updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, userDTO.getPassword()); // This should be the hashed password
            updateStatement.setInt(2, userDTO.getUserId());

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                existingUser.setStatus("ACTIVE");
                existingUser.setPassword(null); // Don't return password
                return existingUser;
            } else {
                throw new RuntimeException("Failed to reactivate user with ID: " + userDTO.getUserId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while reactivating user: " + e.getMessage(), e);
        } finally {
            closeResources(resultSet, selectStatement, updateStatement, connection);
        }
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getConnection();

            String sql = "SELECT user_id, f_name, l_name, email, NIC, address, contact, DOB, role, status FROM user WHERE user_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(resultSet.getInt("user_id"));
                userDTO.setFName(resultSet.getString("f_name"));
                userDTO.setLName(resultSet.getString("l_name"));
                userDTO.setEmail(resultSet.getString("email"));
                userDTO.setNic(resultSet.getString("NIC"));
                userDTO.setAddress(resultSet.getString("address"));
                userDTO.setContact(resultSet.getString("contact"));
                userDTO.setDob(resultSet.getDate("DOB").toLocalDate());
                userDTO.setRole(resultSet.getString("role"));
                userDTO.setStatus(resultSet.getString("status"));
                userDTO.setPassword(null); // Don't return password for security

                return userDTO;
            } else {
                throw new RuntimeException("User not found with ID: " + userId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving user: " + e.getMessage(), e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }
}