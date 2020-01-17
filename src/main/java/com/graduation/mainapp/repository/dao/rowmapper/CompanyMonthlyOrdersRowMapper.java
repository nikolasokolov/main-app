package com.graduation.mainapp.repository.dao.rowmapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyMonthlyOrdersRowMapper {
    private String companyName;
    private String user;
    private Integer numberOfOrders;
    private Integer totalPrice;
}
