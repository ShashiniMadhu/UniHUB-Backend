package com.UniHUB.Server.service.Impl;

import com.UniHUB.Server.dao.UserDAO;
import com.UniHUB.Server.dto.UserDTO;
import com.UniHUB.Server.dto.UserLoginDTO;
import com.UniHUB.Server.dto.UserResponseDTO;
import com.UniHUB.Server.service.UserService;
import com.UniHUB.Server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO login(UserLoginDTO loginDTO) {
        UserDTO user = userDAO.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId());

        return new UserResponseDTO(
                user.getUserId(),
                user.getFName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}
