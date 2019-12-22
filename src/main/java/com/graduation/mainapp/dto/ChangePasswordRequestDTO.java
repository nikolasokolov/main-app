package com.graduation.mainapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordRequestDTO {
    private String username;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
