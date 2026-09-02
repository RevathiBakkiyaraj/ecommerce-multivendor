package com.rev.controller;

import com.rev.modal.User;
import com.rev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rev.modal.Address;
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> UserProfileHandler(
            @RequestHeader("Authorization") String jwt
            ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/address")
    public ResponseEntity<User> addAddress(
            @RequestBody Address address,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        user.getAddresses().add(address);

        User savedUser = userService.saveUser(user);

        return ResponseEntity.ok(savedUser);
    }
}
