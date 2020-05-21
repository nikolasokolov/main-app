package com.graduation.mainapp.repository.dao.rowmapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDailyOrdersRowMapper {

    private String companyName;
    private String user;
    private String menuItemName;
    private Integer menuItemPrice;
    private String timePeriod;
    private String dateOfOrder;
    private String comments;
}
