package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.UserDTO;

import java.util.List;

public interface AdminDAO {
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> viewStudents();

    List<UserDTO> viewLecturers();
}
