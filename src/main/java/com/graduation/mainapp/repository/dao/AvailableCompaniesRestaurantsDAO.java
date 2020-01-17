package com.graduation.mainapp.repository.dao;

import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;

import java.util.List;

public interface AvailableCompaniesRestaurantsDAO {
    List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId);
    List<CompanyRowMapper> getCompaniesForRestaurant(Long restaurantId);
}
