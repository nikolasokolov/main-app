package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private String timePeriod;
    private String comments;
    private Long menuItemId;
    private Long userId;
    private Long restaurantId;
}
