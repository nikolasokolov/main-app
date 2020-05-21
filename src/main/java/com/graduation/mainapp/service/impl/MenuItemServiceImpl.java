package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.converter.MenuItemConverter;
import com.graduation.mainapp.domain.FoodType;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.rest.dto.MenuItemDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final MenuItemConverter menuItemConverter;

    @Override
    public List<MenuItem> getRestaurantMenuItems(Long userId) throws NotFoundException {
        User user = userService.getUser(userId);
        return menuItemRepository.findAllByRestaurant(user.getRestaurant());
    }

    @Override
    public void deleteMenuItem(Long menuItemId) throws NotFoundException {
        MenuItem menuItem = getMenuItem(menuItemId);
        menuItemRepository.delete(menuItem);
    }

    @Override
    public MenuItem createMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException {
        User user = userService.getUser(userId);
        Long restaurantId = user.getRestaurant().getId();
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        MenuItem menuItem = menuItemConverter.convertToMenuItem(menuItemDTO, restaurant);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException {
        User user = userService.getUser(userId);
        Long restaurantId = user.getRestaurant().getId();
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        MenuItem menuItem = getUpdatedMenuItem(menuItemDTO, restaurant);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public List<MenuItem> getRestaurantMenu(Long restaurantId) throws NotFoundException {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        return menuItemRepository.findAllByRestaurant(restaurant);
    }

    @Override
    public MenuItem getMenuItem(Long menuItemId) throws NotFoundException {
        return menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new NotFoundException("Menu Item with ID " + menuItemId + " is not found"));
    }

    private MenuItem getUpdatedMenuItem(MenuItemDTO menuItemDTO, Restaurant restaurant) {
        return MenuItem.builder()
                .id(menuItemDTO.getId())
                .name(menuItemDTO.getName())
                .foodType(FoodType.valueOf(menuItemDTO.getType()))
                .price(menuItemDTO.getPrice())
                .allergens(menuItemDTO.getAllergens())
                .isAvailable(menuItemDTO.getIsAvailable())
                .restaurant(restaurant)
                .build();
    }
}
