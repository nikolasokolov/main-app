package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDTO {

    private Long id;
    private String menuItemName;
    private String timePeriod;
    private String comments;
    private Long restaurantId;
}
