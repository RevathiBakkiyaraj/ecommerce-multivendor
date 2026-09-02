package com.rev.service;

import com.rev.domain.USER_ROLE;
import com.rev.request.LoginRequest;
import com.rev.response.AuthResponse;
import com.rev.response.SignupRequest;

public interface AuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignupRequest req) throws Exception;

    AuthResponse signing(LoginRequest req) throws Exception;
}
