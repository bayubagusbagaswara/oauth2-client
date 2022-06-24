package com.spring.client.service;

import com.spring.client.dto.UserDTO;
import com.spring.client.entity.User;
import com.spring.client.entity.VerificationToken;

public interface UserService {

    User registerUser(UserDTO userDTO);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    User findUserByEmail(String email);
}
