package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.dto.CompanyOrdersResponseDTO;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.UserOrderResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;

import java.util.List;

public interface OrderService {
    Order save(OrderDTO orderDTO) throws DomainObjectNotFoundException;

    Order findByUser(Long userId) throws DomainObjectNotFoundException;

    UserOrderResponseDTO createUserOrderResponseDTO(Order order);

    Order findByIdOrThrow(Long id) throws DomainObjectNotFoundException;

    void delete(Long orderId) throws DomainObjectNotFoundException;

    List<CompanyOrdersResponseDTO> getOrdersForCompany(Long companyId);
}
