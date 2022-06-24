package com.spring.client.service;

import com.spring.client.entity.User;
import com.spring.client.entity.VerificationToken;

public interface VerificationTokenService {

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);
}
