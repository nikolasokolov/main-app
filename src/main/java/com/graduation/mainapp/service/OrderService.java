package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.UserOrderResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;

import java.util.Optional;

public interface OrderService {
    Order save(OrderDTO orderDTO) throws DomainObjectNotFoundException;

    OrderDTO createOrderDTO(Order order);

    Order findByUser(Long userId) throws DomainObjectNotFoundException;

    UserOrderResponseDTO createUserOrderResponseDTO(Order order);

    Order findByIdOrThrow(Long id) throws DomainObjectNotFoundException;
}
