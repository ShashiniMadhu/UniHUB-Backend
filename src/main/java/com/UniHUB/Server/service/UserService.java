package com.UniHUB.Server.service;

import com.UniHUB.Server.dto.*;

public interface UserService {

    UserResponseDTO login(UserLoginDTO loginDTO);
    void generateResetToken(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
