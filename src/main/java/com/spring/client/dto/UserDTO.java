package com.spring.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String matchingPassword;

    // kita bisa menerapkan validasi terhadap password dan matchingPassword
    // karena password dan matchingPassword harusnya bernilai sama
}
