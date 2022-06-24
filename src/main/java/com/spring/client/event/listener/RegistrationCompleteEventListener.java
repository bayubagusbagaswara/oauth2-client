package com.spring.client.event.listener;

import com.spring.client.entity.User;
import com.spring.client.event.RegistrationCompleteEvent;
import com.spring.client.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final VerificationTokenService verificationTokenService;

    @Autowired
    public RegistrationCompleteEventListener(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.saveVerificationTokenForUser(token, user);

        // send mail to user
        String url = event.getApplicationUrl()
                + "/verifyRegistration?token="
                + token;

        // sendVerificationEmail()
        log.info("Click the link to verify your account: {}", url);
    }

}
