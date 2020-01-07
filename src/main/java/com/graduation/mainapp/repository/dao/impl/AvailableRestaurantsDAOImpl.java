package com.graduation.mainapp.repository.dao.impl;

import com.graduation.mainapp.repository.dao.AvailableRestaurantsDAO;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AvailableRestaurantsDAOImpl implements AvailableRestaurantsDAO {
    private static final BeanPropertyRowMapper<AvailableRestaurantsRowMapper> AVAILABLE_RESTAURANTS_ROW_MAPPER =
            new BeanPropertyRowMapper<>(AvailableRestaurantsRowMapper.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId) {
        String availableRestaurantsForCompanyQuery = getAvailableRestaurantsForCompanyQuery(companyId);
        return namedParameterJdbcTemplate.query(availableRestaurantsForCompanyQuery, AVAILABLE_RESTAURANTS_ROW_MAPPER);
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
}
