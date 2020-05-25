package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.rest.dto.MenuItemDTO;

import java.util.List;

public interface MenuItemService {

    List<MenuItem> getRestaurantMenuItems(Long restaurantId) throws NotFoundException;

    void deleteMenuItem(Long menuItemId) throws NotFoundException;

    MenuItem createMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException;

    MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException;

    List<MenuItem> getRestaurantMenu(Long restaurantId) throws NotFoundException;

    MenuItem getMenuItem(Long menuItemId) throws NotFoundException;
}
