package com.graduation.mainapp.converter;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.CompanyOrdersResponseDTO;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.RestaurantDailyOrdersResponseDTO;
import com.graduation.mainapp.dto.UserOrderResponseDTO;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    public UserOrderResponseDTO convertToUserOrderResponseDTO(Order order) {
        return UserOrderResponseDTO.builder()
                .id(order.getId())
                .menuItemName(order.getMenuItem().getName())
                .timePeriod(order.getTimePeriod().toString())
                .comments(order.getComments())
                .restaurantId(order.getMenuItem().getRestaurant().getId())
                .build();
    }

    public List<CompanyOrdersResponseDTO> convertToCompanyOrdersResponseDTOs(Collection<CompanyOrdersRowMapper> companyOrders) {
        return companyOrders.stream()
                .map(companyOrder ->
                        CompanyOrdersResponseDTO.builder()
                                .username(companyOrder.getUsername())
                                .restaurantName(companyOrder.getRestaurantName())
                                .menuItemName(companyOrder.getMenuItemName())
                                .menuItemPrice(companyOrder.getMenuItemPrice())
                                .timePeriod(companyOrder.getTimePeriod())
                                .dateOfOrder(companyOrder.getDateOfOrder())
                                .comments(companyOrder.getComments())
                                .build())
                .collect(Collectors.toList());
    }

    public List<RestaurantDailyOrdersResponseDTO> convertToRestaurantDailyOrdersDTOs(Collection<RestaurantDailyOrdersRowMapper> restaurantDailyOrders) {
        return restaurantDailyOrders.stream()
                .map(order ->
                        RestaurantDailyOrdersResponseDTO.builder()
                                .companyName(order.getCompanyName())
                                .user(order.getUser())
                                .menuItemName(order.getMenuItemName())
                                .menuItemPrice(order.getMenuItemPrice())
                                .timePeriod(order.getTimePeriod())
                                .dateOfOrder(order.getDateOfOrder())
                                .comments(order.getComments())
                                .build())
                .collect(Collectors.toList());
    }

    public Order convertToOrder(OrderDTO orderDTO, User user, MenuItem menuItem) {
        return Order.builder()
                .menuItem(menuItem)
                .user(user)
                .comments(orderDTO.getComments())
                .timePeriod(LocalTime.parse(orderDTO.getTimePeriod(), DateTimeFormatter.ISO_TIME))
                .dateOfOrder(LocalDate.now())
                .build();
    }
}
