package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.UserOrderResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.repository.OrderRepository;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.OrderService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;
    private final RestaurantService restaurantService;

    @Override
    public Order save(OrderDTO orderDTO) throws DomainObjectNotFoundException {
        if (Objects.isNull(orderDTO.getId())) {
            Order order = createOrderObjectForSaving(orderDTO);
            return orderRepository.save(order);
        } else {
            Order orderForUpdating = createOrderObjectForUpdate(orderDTO);
            return orderRepository.save(orderForUpdating);
        }
    }

    private Order createOrderObjectForUpdate(OrderDTO orderDTO) throws DomainObjectNotFoundException {
        Order order = this.findByIdOrThrow(orderDTO.getId());
        MenuItem menuItem = menuItemService.findByIdOrThrow(orderDTO.getMenuItemId());
        order.setComments(orderDTO.getComments());
        order.setMenuItem(menuItem);
        order.setTimePeriod(LocalTime.parse(orderDTO.getTimePeriod(), DateTimeFormatter.ISO_TIME));
        return order;
    }

    @Override
    public OrderDTO createOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .comments(order.getComments())
                .menuItemId(order.getMenuItem().getId())
                .restaurantId(order.getRestaurant().getId())
                .userId(order.getUser().getId())
                .timePeriod(order.getTimePeriod().toString())
                .build();
    }

    @Override
    @Transactional
    public Order findByUser(Long userId) throws DomainObjectNotFoundException {
        User user = userService.findByIdOrThrow(userId);
        return orderRepository.findByUserAndDateOfOrder(user, LocalDate.now());
    }

    @Override
    public UserOrderResponseDTO createUserOrderResponseDTO(Order order) {
        return UserOrderResponseDTO.builder()
                .id(order.getId())
                .menuItemName(order.getMenuItem().getName())
                .timePeriod(order.getTimePeriod().toString())
                .comments(order.getComments())
                .build();
    }

    @Override
    public Order findByIdOrThrow(Long id) throws DomainObjectNotFoundException {
        return orderRepository.findById(id).orElseThrow(
                () -> new DomainObjectNotFoundException("Order with ID " + id + " was not found"));
    }

    private Order createOrderObjectForSaving(OrderDTO orderDTO) throws DomainObjectNotFoundException {
        User user = userService.findByIdOrThrow(orderDTO.getUserId());
        MenuItem menuItem = menuItemService.findByIdOrThrow(orderDTO.getMenuItemId());
        Restaurant restaurant = restaurantService.findByIdOrThrow(orderDTO.getRestaurantId());
        return Order.builder()
                .menuItem(menuItem)
                .restaurant(restaurant)
                .user(user)
                .comments(orderDTO.getComments())
                .timePeriod(LocalTime.parse(orderDTO.getTimePeriod(), DateTimeFormatter.ISO_TIME))
                .dateOfOrder(LocalDate.now())
                .build();
    }
}
