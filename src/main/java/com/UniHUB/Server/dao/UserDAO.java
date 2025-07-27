package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.UserDTO;

import java.util.Optional;

public interface UserDAO {

    Optional<UserDTO> findByEmail(String email);
}
