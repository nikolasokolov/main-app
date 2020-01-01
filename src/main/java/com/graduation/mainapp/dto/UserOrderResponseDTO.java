package com.graduation.mainapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderResponseDTO {
    private Long id;
    private String menuItemName;
    private String timePeriod;
    private String comments;
}
