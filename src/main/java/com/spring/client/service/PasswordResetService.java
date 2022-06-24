package com.spring.client.service;

import com.spring.client.entity.User;

public interface PasswordResetService {

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

}
