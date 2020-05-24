package com.graduation.mainapp.rest.dto;

import com.graduation.mainapp.validation.annotation.PasswordsMatchValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordsMatchValidation(password = "password", confirmPassword = "confirmPassword")
public class RestaurantAccountDTO {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
