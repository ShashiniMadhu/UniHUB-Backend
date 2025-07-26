package com.UniHUB.Server.dao.Impl;

import com.UniHUB.Server.dao.AdminDAO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<UserDTO> viewStudents() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<UserDTO> students = new ArrayList<>();

        try {
            connection = databaseConnection.getConnection();

            String sql = """
                SELECT u.user_id, u.f_name, u.l_name, u.email, u.NIC, u.address, 
                       u.contact, u.DOB, u.role, s.student_id
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
                       u.contact, u.DOB, u.role, l.lecturer_id
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
}