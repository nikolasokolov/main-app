package com.graduation.mainapp.repository.dao;

import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;

import java.util.List;

public interface AvailableRestaurantsDAO {
    List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId);
}
