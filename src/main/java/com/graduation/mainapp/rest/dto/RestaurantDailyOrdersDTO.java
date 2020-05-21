package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDailyOrdersDTO {

    private String companyName;
    private String user;
    private String menuItemName;
    private Integer menuItemPrice;
    private String timePeriod;
    private String dateOfOrder;
    private String comments;
}
