package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.rest.dto.CompanyOrdersDTO;
import com.graduation.mainapp.rest.dto.OrderDTO;
import com.graduation.mainapp.rest.dto.RestaurantDailyOrdersDTO;
import com.graduation.mainapp.exception.NotFoundException;

import java.util.List;

public interface OrderService {

    Order save(OrderDTO orderDTO) throws NotFoundException;

    Order getDailyOrderForUser(Long userId) throws NotFoundException;

    Order getOrder(Long id) throws NotFoundException;

    void deleteOrder(Long orderId) throws NotFoundException;

    List<CompanyOrdersDTO> getOrdersForCompany(Long companyId);

    List<CompanyOrdersDTO> getDailyOrdersForCompany(Long companyId);

    List<RestaurantDailyOrdersDTO> getDailyOrdersForRestaurant(Long restaurantId) throws NotFoundException;
}
