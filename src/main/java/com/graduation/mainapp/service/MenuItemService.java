package com.graduation.mainapp.service;

import com.graduation.mainapp.model.MenuItem;
import com.graduation.mainapp.web.dto.MenuItemsDTO;

import java.util.Collection;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getRestaurantMenuItems(Long restaurantId);

    List<MenuItemsDTO> createMenuItemsDTO(Collection<MenuItem> menuItems);

    void delete(Long menuItemId);
}
