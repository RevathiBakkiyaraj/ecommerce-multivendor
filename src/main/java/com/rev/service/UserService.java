package com.rev.service;

import com.rev.modal.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;

    User saveUser(User user);
}
