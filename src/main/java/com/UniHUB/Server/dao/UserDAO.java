package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.UserDTO;
import org.apache.catalina.User;

import java.util.Optional;

public interface UserDAO {

    Optional<UserDTO> findByEmail(String email);
    Optional<UserDTO> findByResetToken(String token);
    void save(UserDTO user);

    // Fetch studentId from Student table using userId

    Integer findStudentIdByUserId(Integer userId);

    // Fetch lecturerId from Lecturer table using userId

    Integer findLecturerIdByUserId( Integer userId);
}
