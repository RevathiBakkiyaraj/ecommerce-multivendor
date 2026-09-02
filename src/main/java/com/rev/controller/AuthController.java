package com.rev.controller;

import com.rev.domain.USER_ROLE;
import com.rev.request.LoginOtpRequest;
import com.rev.request.LoginRequest;
import com.rev.response.ApiResponse;
import com.rev.response.AuthResponse;
import com.rev.repository.UserRepository;
import com.rev.response.SignupRequest;
import com.rev.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserManagerHandler(@RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);
    }

        @PostMapping("/sent/login-signup-otp")
        public ResponseEntity<ApiResponse> sentOtpHandler(
                @RequestBody LoginOtpRequest req) throws Exception {

            authService.sentLoginOtp(req.getEmail(),req.getRole());

            ApiResponse res = new ApiResponse();

            res.setMessage("otp sent successfully");

            return ResponseEntity.ok(res);
        }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.signing(req);

        return ResponseEntity.ok(authResponse);
    }

}

