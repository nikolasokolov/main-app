package com.graduation.mainapp.repository.dao.rowmapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyOrdersRowMapper {
    private String username;
    private String restaurantName;
    private String menuItemName;
    private Integer menuItemPrice;
    private String timePeriod;
    private String dateOfOrder;
    private String comments;
}
