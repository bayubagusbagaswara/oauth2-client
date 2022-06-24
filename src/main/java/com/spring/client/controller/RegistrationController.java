package com.spring.client.controller;

import com.spring.client.dto.PasswordResetDTO;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {

        // validasi token hasil register
        String result = verificationTokenService.validateVerificationToken(token);

        if (result.equalsIgnoreCase("valid")) {
            // berhasil verifikasi, dan user berhasil register
            return "User Verifies Successfully";
        }
        // gagal verifikasi token, dan user gagal register
        return "Bad User";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest httpServletRequest) {

        // generate token baru, karena oldToken sudah expired
        // user belum sempat verifikasi token di email
        // sehingga user harus generate token yang baru lagi
        VerificationToken verificationToken = verificationTokenService.generateNewVerificationToken(oldToken);

        // ambil data user dari verification token
        User user = verificationToken.getUser();

        // kirim ulang verifikasi token ke email user
        resendVerificationTokenMail(
                user,
                applicationUrl(httpServletRequest),
                verificationToken
        );
        // link verifikasi token berhasil dikirim
        return "Verification Link Sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordResetDTO passwordResetDTO, HttpServletRequest httpServletRequest) {

        // ambil data user by email
        User user = userService.findUserByEmail(passwordResetDTO.getEmail());

        String url = "";
        // jika user tidak sama dengan null (alias ada datanya), maka lalukan reset password
        // pertama, kita create token untuk reset password
        // lalu, kita kirim token ke email melalui URL
        if (user != null) {
            String token = UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(
                    user,
                    applicationUrl(httpServletRequest),
                    token
            );
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordResetDTO passwordResetDTO) {

        String result = passwordResetTokenService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }
        Optional<User> user = passwordResetTokenService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            passwordResetTokenService.changePassword(
                    user.get(),
                    passwordResetDTO.getNewPassword()
            );
            return "Password Reset Successfully";

        } else {
            return "Invalid Token";
        }
    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
        // send verification email
        log.info("Click the link to verify your account: {}", url);
    }


    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/savePassword?token="
                        + token;

        log.info("Click the link to Reset your Password: {}", url);
        return url;
    }
}
