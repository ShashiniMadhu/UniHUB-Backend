package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.dto.UserLoginDTO;
import com.UniHUB.Server.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO login(UserLoginDTO loginDTO);
}
