package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.dto.MenuItemDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MenuItemService {
    List<MenuItem> getRestaurantMenuItems(Long restaurantId);

    List<MenuItemDTO> createMenuItemsDTO(Collection<MenuItem> menuItems);

    void delete(Long menuItemId);

    MenuItem addMenuItem(Long userId, MenuItemDTO menuItemDTO) throws DomainObjectNotFoundException;

    MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO) throws DomainObjectNotFoundException;

    MenuItem getMenuItem(Long id);

    List<MenuItem> getRestaurantMenu(Long restaurantId) throws DomainObjectNotFoundException;

    Map<String, List<MenuItemDTO>> createTypeToMenuItemsDTO(Collection<MenuItem> menuItems);

    MenuItem findByIdOrThrow(Long menuItemId) throws DomainObjectNotFoundException;
}
