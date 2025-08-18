package com.UniHUB.Server.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {

    private final String url = "jdbc:mysql://localhost:3306/unihub";
    private final String username = "root";
    private final String password = "";


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;


    public Connection getConnection() {
        Connection connection = null;
        try {
            // Log the actual values being used (mask password for security)
            System.out.println("Attempting to connect to: " + url);
            System.out.println("Username: " + username);
            System.out.println("Driver: " + driverClassName);

            // Load the driver
            Class.forName(driverClassName);

            // Create connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connection successful!");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found: " + driverClassName);
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database.");
            System.err.println("URL: " + url);
            System.err.println("Username: " + username);
            e.printStackTrace();
        }
        return connection;
    }

    public void testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null && connection.isValid(2)) {
                System.out.println("✅ Database connection is valid and open.");
            } else {
                System.out.println("❌ Database connection is invalid or closed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error while validating the database connection.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DatabaseConnection dbConn = new DatabaseConnection();
        dbConn.testConnection();
    }
}