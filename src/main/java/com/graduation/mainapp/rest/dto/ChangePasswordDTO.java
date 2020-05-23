package com.graduation.mainapp.rest.dto;

import com.graduation.mainapp.validation.annotation.PasswordsMatchValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordsMatchValidation(password = "password", confirmPassword = "confirmPassword")
public class ChangePasswordDTO {

    private String username;
    private String oldPassword;
    private String password;
    private String confirmPassword;
}
