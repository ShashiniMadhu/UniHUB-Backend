package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.UserDTO;

public interface AdminService {

    /**
     * create users(Lecturer/Student)
     * @param userDTO
     * @return
     */
    UserDTO createUser(UserDTO userDTO);
}
