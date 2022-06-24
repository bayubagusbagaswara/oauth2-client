package com.spring.client.service;

import com.spring.client.dto.UserDTO;
import com.spring.client.entity.User;

public interface UserService {

    User registerUser(UserDTO userDTO);
}
