package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.MenuItem;
import com.graduation.mainapp.model.User;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.MenuItemsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final UserService userService;

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
    public List<MenuItemsDTO> createMenuItemsDTO(Collection<MenuItem> menuItems) {
        return menuItems.stream().map(menuItem -> MenuItemsDTO.builder()
                .id(menuItem.getId())
                .type(menuItem.getFoodType().toString())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
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
}
