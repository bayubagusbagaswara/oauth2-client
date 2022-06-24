package com.spring.client.service;

import com.spring.client.entity.VerificationToken;

public interface VerificationTokenService {

    VerificationToken generateNewVerificationToken(String oldToken);
}
