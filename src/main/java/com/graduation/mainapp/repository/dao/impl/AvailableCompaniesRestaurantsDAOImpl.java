package com.graduation.mainapp.repository.dao.impl;

import com.graduation.mainapp.repository.dao.AvailableCompaniesRestaurantsDAO;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AvailableCompaniesRestaurantsDAOImpl implements AvailableCompaniesRestaurantsDAO {

    private static final BeanPropertyRowMapper<AvailableRestaurantsRowMapper> AVAILABLE_RESTAURANTS_ROW_MAPPER =
            new BeanPropertyRowMapper<>(AvailableRestaurantsRowMapper.class);
    private static final BeanPropertyRowMapper<CompanyRowMapper> COMPANY_ROW_MAPPER =
            new BeanPropertyRowMapper<>(CompanyRowMapper.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId) {
        String availableRestaurantsForCompanyQuery = getAvailableRestaurantsForCompanyQuery(companyId);
        return namedParameterJdbcTemplate.query(availableRestaurantsForCompanyQuery, AVAILABLE_RESTAURANTS_ROW_MAPPER);
    }

    @Override
    public List<CompanyRowMapper> getCompaniesForRestaurant(Long restaurantId) {
        String companiesForRestaurantQuery = getCompaniesForRestaurantQuery(restaurantId);
        return namedParameterJdbcTemplate.query(companiesForRestaurantQuery, COMPANY_ROW_MAPPER);
    }

    private String getAvailableRestaurantsForCompanyQuery(Long companyId) {
        return "SELECT r.id, r.name, r.address, r.phone_number, r.logo" +
                " FROM restaurant r" +
                " WHERE r.id NOT IN (" +
                " SELECT cr.restaurant_id " +
                " FROM company_restaurant cr" +
                " WHERE cr.company_id = " + companyId +
                ")";
    }

    private String getCompaniesForRestaurantQuery(Long restaurantId) {
        return "SELECT c.*" +
                " FROM company c" +
                " WHERE c.id IN (" +
                " SELECT cr.company_id" +
                " FROM company_restaurant cr WHERE cr.restaurant_id = " + restaurantId + ")";
    }
}
