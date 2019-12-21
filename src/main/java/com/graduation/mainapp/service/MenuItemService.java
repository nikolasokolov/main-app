package com.graduation.mainapp.service;

import com.graduation.mainapp.model.MenuItem;
import com.graduation.mainapp.web.dto.MenuItemDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    List<MenuItem> getRestaurantMenuItems(Long restaurantId);

    List<MenuItemDTO> createMenuItemsDTO(Collection<MenuItem> menuItems);

    void delete(Long menuItemId);

    MenuItem addMenuItem(Long userId, MenuItemDTO menuItemDTO);

    MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO);

    MenuItem getMenuItem(Long id);
}
