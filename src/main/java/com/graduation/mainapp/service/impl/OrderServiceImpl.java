package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.converter.OrderConverter;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.rest.dto.CompanyOrdersDTO;
import com.graduation.mainapp.rest.dto.OrderDTO;
import com.graduation.mainapp.rest.dto.RestaurantDailyOrdersDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.OrderRepository;
import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.RestaurantDailyOrdersRowMapper;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.OrderService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;
    private final OrderDAO orderDAO;
    private final RestaurantService restaurantService;
    private final OrderConverter orderConverter;

    @Override
    public Order save(OrderDTO orderDTO) throws NotFoundException {
        if (Objects.isNull(orderDTO.getId())) {
            User user = userService.getUser(orderDTO.getUserId());
            MenuItem menuItem = menuItemService.getMenuItem(orderDTO.getMenuItemId());
            Order order = orderConverter.convertToOrder(orderDTO, user, menuItem);
            return orderRepository.save(order);
        } else {
            Order orderForUpdating = getUpdatedOrder(orderDTO);
            return orderRepository.save(orderForUpdating);
        }
    }

    @Override
    @Transactional
    public Order getDailyOrderForUser(Long userId) throws NotFoundException {
        User user = userService.getUser(userId);
        return orderRepository.findByUserAndDateOfOrder(user, LocalDate.now());
    }

    @Override
    public Order getOrder(Long id) throws NotFoundException {
        return orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Order with ID " + id + " was not found"));
    }

    @Override
    public void deleteOrder(Long orderId) throws NotFoundException {
        Order order = getOrder(orderId);
        orderRepository.delete(order);
    }

    @Override
    public List<CompanyOrdersDTO> getOrdersForCompany(Long companyId) {
        List<CompanyOrdersRowMapper> companyOrders = orderDAO.getOrdersForCompany(companyId);
        return orderConverter.convertToCompanyOrdersResponseDTOs(companyOrders);
    }

    @Override
    public List<CompanyOrdersDTO> getDailyOrdersForCompany(Long companyId) {
        List<CompanyOrdersRowMapper> dailyCompanyOrders = orderDAO.getDailyOrdersForCompany(companyId);
        return orderConverter.convertToCompanyOrdersResponseDTOs(dailyCompanyOrders);
    }

    @Override
    public List<RestaurantDailyOrdersDTO> getDailyOrdersForRestaurant(Long restaurantAccountId) throws NotFoundException {
        User user = userService.getUser(restaurantAccountId);
        Restaurant restaurant = restaurantService.findByUser(user);
        List<RestaurantDailyOrdersRowMapper> restaurantDailyOrders = orderDAO.getRestaurantDailyOrders(restaurant.getId());
        return orderConverter.convertToRestaurantDailyOrdersDTOs(restaurantDailyOrders);
    }

    private Order getUpdatedOrder(OrderDTO orderDTO) throws NotFoundException {
        Order order = getOrder(orderDTO.getId());
        MenuItem menuItem = menuItemService.getMenuItem(orderDTO.getMenuItemId());
        order.setComments(orderDTO.getComments());
        order.setMenuItem(menuItem);
        order.setTimePeriod(LocalTime.parse(orderDTO.getTimePeriod(), DateTimeFormatter.ISO_TIME));
        return order;
    }
}
