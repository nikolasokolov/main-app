package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.FoodType;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.MenuItemDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
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
    public List<MenuItem> getRestaurantMenuItems(Long userId) throws NotFoundException {
        User user = userService.getUser(userId);
        return menuItemRepository.findAllByRestaurant(user.getRestaurant());
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
    public MenuItem addMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException {
        User user = userService.getUser(userId);
        Long restaurantId = user.getRestaurant().getId();
        Restaurant restaurant = restaurantService.findByIdOrThrow(restaurantId);
        MenuItem menuItem = createMenuItemObject(menuItemDTO, restaurant);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(Long userId, MenuItemDTO menuItemDTO) throws NotFoundException {
        User user = userService.getUser(userId);
        Long restaurantId = user.getRestaurant().getId();
        Restaurant restaurant = restaurantService.findByIdOrThrow(restaurantId);
        MenuItem menuItem = createMenuItemObjectForUpdating(menuItemDTO, restaurant);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem getMenuItem(Long id) {
        return menuItemRepository.getOne(id);
    }

    @Override
    public List<MenuItem> getRestaurantMenu(Long restaurantId) throws NotFoundException {
        Restaurant restaurant = restaurantService.findByIdOrThrow(restaurantId);
        return menuItemRepository.findAllByRestaurant(restaurant);
    }

    @Override
    public Map<String, List<MenuItemDTO>> createTypeToMenuItemsDTO(Collection<MenuItem> menuItems) {
        List<MenuItemDTO> menuItemDTOs = createMenuItemsDTO(menuItems);
        return menuItemDTOs.stream().filter(MenuItemDTO::getIsAvailable).collect(Collectors.groupingBy(MenuItemDTO::getType));
    }

    @Override
    public MenuItem findByIdOrThrow(Long menuItemId) throws NotFoundException {
        return menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new NotFoundException("Menu Item with ID " + menuItemId + " is not found"));
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
