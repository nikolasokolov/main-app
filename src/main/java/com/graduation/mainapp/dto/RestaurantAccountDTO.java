package com.graduation.mainapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantAccountDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
