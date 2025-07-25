package com.UniHUB.Server.dao;

import com.UniHUB.Server.dto.UserDTO;

public interface AdminDAO {
    UserDTO createUser(UserDTO userDTO);
}
