package com.graduation.mainapp.repository.dao.impl;

import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OrderDAOImpl implements OrderDAO {
    private static final BeanPropertyRowMapper<CompanyOrdersRowMapper> COMPANY_ORDERS__ROW_MAPPER =
            new BeanPropertyRowMapper<>(CompanyOrdersRowMapper.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<CompanyOrdersRowMapper> getOrdersForCompany(Long companyId) {
        String ordersForCompanyQuery = getOrdersForCompanyQuery();
        return namedParameterJdbcTemplate.query(ordersForCompanyQuery, COMPANY_ORDERS__ROW_MAPPER);
    }

    private String getOrdersForCompanyQuery() {
        return "SELECT DISTINCT u.username, r.name as restaurant_name, mi.name as menu_item_name," +
                " mi.price as menu_item_price, o.time_period, o.date_of_order, o.comments" +
                " FROM orders o" +
                " INNER JOIN users u ON o.user_id = u.id" +
                " INNER JOIN menu_item mi ON o.menu_item_id = mi.id" +
                " INNER JOIN restaurant r on r.id = mi.restaurant_id";
    }
}
