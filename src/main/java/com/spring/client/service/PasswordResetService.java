package com.spring.client.service;

import com.spring.client.entity.User;

import java.util.Optional;

public interface PasswordResetService {

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);
}
