package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.dto.MenuItemDTO;

import java.util.Collection;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getRestaurantMenuItems(Long restaurantId);

    List<MenuItemDTO> createMenuItemsDTO(Collection<MenuItem> menuItems);

    void delete(Long menuItemId);

    MenuItem addMenuItem(Long userId, MenuItemDTO menuItemDTO);

    MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO);

    MenuItem getMenuItem(Long id);
}
