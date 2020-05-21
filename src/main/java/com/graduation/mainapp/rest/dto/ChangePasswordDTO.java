package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDTO {

    private String username;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
