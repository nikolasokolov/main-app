package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.FoodType;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.dto.MenuItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @Override
    public List<MenuItem> getRestaurantMenuItems(Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            return menuItemRepository.findAllByRestaurant(user.get().getRestaurant());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Override
    public List<MenuItemDTO> createMenuItemsDTO(Collection<MenuItem> menuItems) {
        return menuItems.stream().map(menuItem -> MenuItemDTO.builder()
                .id(menuItem.getId())
                .type(menuItem.getFoodType().toString())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .allergens(menuItem.getAllergens())
                .isAvailable(menuItem.getIsAvailable())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void delete(Long menuItemId) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemId);
        if (menuItem.isPresent()) {
            menuItemRepository.delete(menuItem.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found");
        }
    }

    @Override
    public MenuItem addMenuItem(Long userId, MenuItemDTO menuItemDTO) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            Long restaurantId = user.get().getRestaurant().getId();
            Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
            MenuItem menuItem = createMenuItemObject(menuItemDTO, restaurant.get());
            return menuItemRepository.save(menuItem);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Override
    public MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            Long restaurantId = user.get().getRestaurant().getId();
            Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
            MenuItem menuItem = createMenuItemObjectForUpdating(menuItemDTO, restaurant.get());
            return menuItemRepository.save(menuItem);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Override
    public MenuItem getMenuItem(Long id) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        if (menuItem.isPresent()) {
            return menuItem.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Item not found");
        }
    }

    @Override
    public List<MenuItem> getRestaurantMenu(Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            return menuItemRepository.findAllByRestaurant(restaurant.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
    }

    @Override
    public Map<String, List<MenuItemDTO>> createTypeToMenuItemsDTO(Collection<MenuItem> menuItems) {
        List<MenuItemDTO> menuItemDTOs = createMenuItemsDTO(menuItems);
        return menuItemDTOs.stream().filter(MenuItemDTO::getIsAvailable).collect(Collectors.groupingBy(MenuItemDTO::getType));
    }

    private MenuItem createMenuItemObjectForUpdating(MenuItemDTO menuItemDTO, Restaurant restaurant) {
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

    private MenuItem createMenuItemObject(MenuItemDTO menuItemDTO, Restaurant restaurant) {
        return MenuItem.builder()
                .name(menuItemDTO.getName())
                .foodType(FoodType.valueOf(menuItemDTO.getType()))
                .price(menuItemDTO.getPrice())
                .restaurant(restaurant)
                .allergens(menuItemDTO.getAllergens())
                .isAvailable(menuItemDTO.getIsAvailable())
                .build();
    }

}
