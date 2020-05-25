package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import com.graduation.mainapp.rest.dto.CompanyDTO;

import java.util.List;
import java.util.Set;

public interface CompanyRestaurantService {

    void addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId) throws NotFoundException;

    void deleteRestaurantForCompany(Long companyId, Long restaurantId) throws NotFoundException;

    Set<Restaurant> getRestaurantsForCompany(Long companyId) throws NotFoundException;

    List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId);

    List<CompanyRowMapper> getCompaniesForRestaurant(Long userId) throws NotFoundException;
}
