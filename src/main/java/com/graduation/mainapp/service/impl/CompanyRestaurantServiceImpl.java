package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.dao.AvailableCompaniesRestaurantsDAO;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import com.graduation.mainapp.service.CompanyRestaurantService;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyRestaurantServiceImpl implements CompanyRestaurantService {

    private final UserService userService;
    private final CompanyService companyService;
    private final RestaurantService restaurantService;
    private final AvailableCompaniesRestaurantsDAO availableCompaniesRestaurantsDAO;

    @Override
    @Transactional
    public void addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId) throws NotFoundException {
        Company company = companyService.getCompany(companyDTO.getId());
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        company.getRestaurants().add(restaurant);
        restaurant.getCompanies().add(company);
        companyService.save(company);
        restaurantService.save(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurantForCompany(Long companyId, Long restaurantId) throws NotFoundException {
        Company company = companyService.getCompany(companyId);
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        restaurant.removeCompany(company);
        company.removeRestaurant(restaurant);
        companyService.save(company);
        restaurantService.save(restaurant);
    }

    @Override
    public List<CompanyRowMapper> getCompaniesForRestaurant(Long userId) throws NotFoundException {
        User user = userService.getUser(userId);
        return availableCompaniesRestaurantsDAO.getCompaniesForRestaurant(user.getRestaurant().getId());
    }

    @Override
    public List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId) {
        return availableCompaniesRestaurantsDAO.getAvailableRestaurantsForCompany(companyId);
    }

    @Override
    public Set<Restaurant> getRestaurantsForCompany(Long companyId) throws NotFoundException {
        Company company = companyService.getCompany(companyId);
        return company.getRestaurants();
    }
}
