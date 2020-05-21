package com.graduation.mainapp.converter;

import com.graduation.mainapp.domain.FoodType;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.rest.dto.MenuItemDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuItemConverter {

    public List<MenuItemDTO> convertToMenuItemDTOs(Collection<MenuItem> menuItems) {
        return menuItems.stream()
                .map(this::convertToMenuItemDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO convertToMenuItemDTO(MenuItem menuItem) {
        return MenuItemDTO.builder()
                .id(menuItem.getId())
                .type(menuItem.getFoodType().toString())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .allergens(menuItem.getAllergens())
                .isAvailable(menuItem.getIsAvailable())
                .build();
    }

    public MenuItem convertToMenuItem(MenuItemDTO menuItemDTO, Restaurant restaurant) {
        return MenuItem.builder()
                .name(menuItemDTO.getName())
                .foodType(FoodType.valueOf(menuItemDTO.getType()))
                .price(menuItemDTO.getPrice())
                .restaurant(restaurant)
                .allergens(menuItemDTO.getAllergens())
                .isAvailable(menuItemDTO.getIsAvailable())
                .build();
    }

    public Map<String, List<MenuItemDTO>> createTypeToMenuItemsDTO(Collection<MenuItem> menuItems) {
        List<MenuItemDTO> menuItemDTOs = convertToMenuItemDTOs(menuItems);
        return menuItemDTOs.stream()
                .filter(MenuItemDTO::getIsAvailable)
                .collect(Collectors.groupingBy(MenuItemDTO::getType));
    }
}
