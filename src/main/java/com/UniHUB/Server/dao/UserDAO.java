package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.UserDTO;

import java.util.Optional;

public interface UserDAO {

    Optional<UserDTO> findByEmail(String email);


    // Fetch studentId from Student table using userId

    Integer findStudentIdByUserId(Integer userId);

    // Fetch lecturerId from Lecturer table using userId

    Integer findLecturerIdByUserId( Integer userId);
}
