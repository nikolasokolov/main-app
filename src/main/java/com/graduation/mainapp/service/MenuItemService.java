package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.FoodType;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.dto.MenuItemDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MenuItemService {
    List<MenuItem> getRestaurantMenuItems(Long restaurantId);

    List<MenuItemDTO> createMenuItemsDTO(Collection<MenuItem> menuItems);

    void delete(Long menuItemId);

    MenuItem addMenuItem(Long userId, MenuItemDTO menuItemDTO);

    MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO);

    MenuItem getMenuItem(Long id);

    List<MenuItem> getRestaurantMenu(Long restaurantId);

    Map<String, List<MenuItemDTO>> createTypeToMenuItemsDTO(Collection<MenuItem> menuItems);
}
