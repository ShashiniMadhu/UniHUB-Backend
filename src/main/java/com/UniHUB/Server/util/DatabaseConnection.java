package com.UniHUB.Server.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    private final String url = "jdbc:mysql://localhost:3307/unihub";
    private final String username = "root";
    private final String password = "";

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful!");
        } catch (ClassNotFoundException exception) {
            System.err.println("JDBC Driver not found.");
            exception.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database.");
            e.printStackTrace();
        }
        return connection;
    }

    // Test method to check the connection
    public void testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null && connection.isValid(2)) { // timeout 2 seconds
                System.out.println("Database connection is valid and open.");
            } else {
                System.out.println("Database connection is invalid or closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error while validating the database connection.");
            e.printStackTrace();
        }
    }

    // Main method for quick testing
    public static void main(String[] args) {
        DatabaseConnection dbConn = new DatabaseConnection();
        dbConn.testConnection();
    }
}

