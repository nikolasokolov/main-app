package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.dto.MenuItemDTO;
import com.graduation.mainapp.exception.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MenuItemService {

    List<MenuItem> getRestaurantMenuItems(Long restaurantId) throws NotFoundException;

    void deleteMenuItem(Long menuItemId) throws NotFoundException;

    MenuItem createMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException;

    MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException;

    List<MenuItem> getRestaurantMenu(Long restaurantId) throws NotFoundException;

    MenuItem getMenuItem(Long menuItemId) throws NotFoundException;
}
