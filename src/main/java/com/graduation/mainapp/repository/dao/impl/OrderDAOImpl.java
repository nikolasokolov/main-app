package com.graduation.mainapp.repository.dao.impl;

import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyMonthlyOrdersRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OrderDAOImpl implements OrderDAO {
    private static final BeanPropertyRowMapper<CompanyOrdersRowMapper> COMPANY_ORDERS__ROW_MAPPER =
            new BeanPropertyRowMapper<>(CompanyOrdersRowMapper.class);
    private static final BeanPropertyRowMapper<RestaurantDailyOrdersRowMapper> DAILY_RESTAURANT_ORDERS_ROW_MAPPER =
            new BeanPropertyRowMapper<>(RestaurantDailyOrdersRowMapper.class);
    private static final BeanPropertyRowMapper<CompanyMonthlyOrdersRowMapper> MONTHLY_COMPANY_ORDERS_ROW_MAPPER =
            new BeanPropertyRowMapper<>(CompanyMonthlyOrdersRowMapper.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<CompanyOrdersRowMapper> getOrdersForCompany(Long companyId) {
        String ordersForCompanyQuery = getOrdersForCompanyQuery(companyId);
        return namedParameterJdbcTemplate.query(ordersForCompanyQuery, COMPANY_ORDERS__ROW_MAPPER);
    }

    @Override
    public List<CompanyOrdersRowMapper> getDailyOrdersForCompany(Long companyId) {
        LocalDate currentDate = LocalDate.now();
        String dailyOrdersForCompanyQuery = getDailyOrdersForCompanyQuery(currentDate.toString(), companyId);
        return namedParameterJdbcTemplate.query(dailyOrdersForCompanyQuery, COMPANY_ORDERS__ROW_MAPPER);
    }

    @Override
    public List<RestaurantDailyOrdersRowMapper> getRestaurantDailyOrders(Long restaurantId) {
        LocalDate currentDate = LocalDate.now();
        String restaurantDailyOrdersQuery = getDailyOrdersForRestaurant(currentDate.toString(), restaurantId);
        return namedParameterJdbcTemplate.query(restaurantDailyOrdersQuery, DAILY_RESTAURANT_ORDERS_ROW_MAPPER);
    }

    @Override
    public List<CompanyMonthlyOrdersRowMapper> getMonthlyOrdersForCompany(Long companyId, Long restaurantId) {
        String monthlyOrdersForCompanyQuery = getMonthlyOrdersForCompanyQuery(companyId, restaurantId, LocalDate.now().getMonthValue());
        return namedParameterJdbcTemplate.query(monthlyOrdersForCompanyQuery, MONTHLY_COMPANY_ORDERS_ROW_MAPPER);
    }

    private String getDailyOrdersForCompanyQuery(String currentDate, Long companyId) {
        return "SELECT DISTINCT u.username, r.name as restaurant_name, mi.name as menu_item_name," +
                " mi.price as menu_item_price, o.time_period, o.date_of_order, o.comments" +
                " FROM orders o" +
                " INNER JOIN users u ON o.user_id = u.id" +
                " INNER JOIN menu_item mi ON o.menu_item_id = mi.id" +
                " INNER JOIN restaurant r on r.id = mi.restaurant_id" +
                " WHERE o.date_of_order = '" + currentDate + "'::date" +
                " AND u.company_id = " + companyId;
    }

    private String getOrdersForCompanyQuery(Long companyId) {
        return "SELECT DISTINCT u.username, r.name as restaurant_name, mi.name as menu_item_name," +
                " mi.price as menu_item_price, o.time_period, o.date_of_order, o.comments" +
                " FROM orders o" +
                " INNER JOIN users u ON o.user_id = u.id" +
                " INNER JOIN menu_item mi ON o.menu_item_id = mi.id" +
                " INNER JOIN restaurant r on r.id = mi.restaurant_id" +
                " WHERE u.company_id = " + companyId;

    }

    private String getDailyOrdersForRestaurant(String currentDate, Long restaurantId) {
        return "SELECT DISTINCT" +
                " c.name as company_name, u.username as user, mi.name as menu_item_name," +
                " mi.price as menu_item_price, o.time_period, o.date_of_order, o.comments" +
                " FROM orders o \n" +
                " INNER JOIN users u ON o.user_id = u.id" +
                " INNER JOIN menu_item mi ON o.menu_item_id = mi.id" +
                " INNER JOIN restaurant r on r.id = mi.restaurant_id" +
                " INNER JOIN company c on c.id = u.company_id" +
                " WHERE o.date_of_order = '" + currentDate + "'::date" +
                " AND r.id = " + restaurantId +
                " ORDER BY " +
                " c.name, time_period";
    }

    private String getMonthlyOrdersForCompanyQuery(Long companyId, Long restaurantId, Integer currentMonth) {
        return "SELECT c.name as company_name, u.username as user, SUM(mi.price) as total_price, count(*) as number_of_orders" +
                " FROM orders o " +
                " INNER JOIN users u ON o.user_id = u.id" +
                " INNER JOIN menu_item mi ON o.menu_item_id = mi.id" +
                " INNER JOIN restaurant r on r.id = mi.restaurant_id" +
                " INNER JOIN company c on c.id = u.company_id AND EXTRACT(MONTH FROM o.date_of_order) = " + currentMonth +
                " WHERE r.id = " + restaurantId + " AND c.id = " + companyId +
                " GROUP BY c.name, u.username" +
                " ORDER BY c.name, u.username";
    }
}
