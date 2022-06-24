package com.spring.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetDTO {

    private String email;
    private String oldPassword;
    private String newPassword;
}
