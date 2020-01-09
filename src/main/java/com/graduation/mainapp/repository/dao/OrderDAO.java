package com.graduation.mainapp.repository.dao;

import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;

import java.util.List;

public interface OrderDAO {
    List<CompanyOrdersRowMapper> getOrdersForCompany(Long companyId);

    List<CompanyOrdersRowMapper> getDailyOrdersForCompany(Long companyId);

    List<RestaurantDailyOrdersRowMapper> getRestaurantDailyOrders(Long restaurantId);
}
