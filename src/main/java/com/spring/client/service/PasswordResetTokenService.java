package com.spring.client.service;

import com.spring.client.entity.User;

import java.util.Optional;

public interface PasswordResetTokenService {

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
