package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.dto.CompanyOrdersResponseDTO;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.RestaurantDailyOrdersResponseDTO;
import com.graduation.mainapp.exception.NotFoundException;

import java.util.List;

public interface OrderService {

    Order save(OrderDTO orderDTO) throws NotFoundException;

    Order getDailyOrderForUser(Long userId) throws NotFoundException;

    Order getOrder(Long id) throws NotFoundException;

    void deleteOrder(Long orderId) throws NotFoundException;

    List<CompanyOrdersResponseDTO> getOrdersForCompany(Long companyId);

    List<CompanyOrdersResponseDTO> getDailyOrdersForCompany(Long companyId);

    List<RestaurantDailyOrdersResponseDTO> getDailyOrdersForRestaurant(Long restaurantId) throws NotFoundException;
}
