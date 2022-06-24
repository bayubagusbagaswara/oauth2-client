package com.spring.client.controller;

import com.spring.client.dto.UserDTO;
import com.spring.client.entity.User;
import com.spring.client.entity.VerificationToken;
import com.spring.client.event.RegistrationCompleteEvent;
import com.spring.client.service.PasswordResetTokenService;
import com.spring.client.service.UserService;
import com.spring.client.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class RegistrationController {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public RegistrationController(UserService userService, VerificationTokenService verificationTokenService, PasswordResetTokenService passwordResetTokenService, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO, final HttpServletRequest httpServletRequest) {
        User user = userService.registerUser(userDTO);
        // publish event yang berisi data User yang register dan URL untuk request register
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(httpServletRequest)
        ));
        return "Success Register User";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
