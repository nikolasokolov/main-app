package com.graduation.mainapp.rest;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.dto.MenuItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/main")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MenuItemResource {
    private final MenuItemService menuItemService;

    @RequestMapping(value = "/restaurant/{userId}/menu-items", method = RequestMethod.GET)
    public ResponseEntity<?> getRestaurantMenuItems(@PathVariable Long userId) {
        log.info("Received request for fetching menu items for restaurant account with ID [{}]", userId);
        List<MenuItem> menuItems = menuItemService.getRestaurantMenuItems(userId);
        List<MenuItemDTO> menuItemDTO = menuItemService.createMenuItemsDTO(menuItems);
        log.info("Finished fetching menu items for restaurant account with ID [{}]", userId);
        return new ResponseEntity<>(menuItemDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/menu-items/{menuItemId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long menuItemId) {
        log.info("Received request for deleting menu item with ID [{}]", menuItemId);
        menuItemService.delete(menuItemId);
        log.info("Successfully deleted menu item for with ID [{}]", menuItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/{userId}/menu-items/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMenuItem(@PathVariable Long userId, @RequestBody MenuItemDTO menuItemDTO) {
        log.info("Received request for adding menu item for restaurant account with ID [{}]", userId);
        MenuItem menuItem = menuItemService.addMenuItem(userId, menuItemDTO);
        List<MenuItemDTO> menuItemDTOResponse = menuItemService.createMenuItemsDTO(Collections.singletonList(menuItem));
        log.info("Successfully added menu item for restaurant account with ID [{}]", userId);
        return new ResponseEntity<>(menuItemDTOResponse.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/{userId}/menu-items/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMenuItem(@PathVariable Long userId, @RequestBody MenuItemDTO menuItemDTO) {
        log.info("Received request for updating menu item for restaurant account with ID [{}]", userId);
        MenuItem menuItem = menuItemService.updateMenuItem(userId, menuItemDTO);
        List<MenuItemDTO> menuItemDTOResponse = menuItemService.createMenuItemsDTO(Collections.singletonList(menuItem));
        log.info("Successfully updated menu item for restaurant account with ID [{}]", userId);
        return new ResponseEntity<>(menuItemDTOResponse.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/menu-items/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMenuItem(@PathVariable Long id) {
        log.info("Received request for fetching menu item with ID [{}]", id);
        MenuItem menuItem = menuItemService.getMenuItem(id);
        List<MenuItemDTO> menuItemDTO = menuItemService.createMenuItemsDTO(Collections.singletonList(menuItem));
        log.info("Finished fetching menu item for with ID [{}]", id);
        return new ResponseEntity<>(menuItemDTO.get(0), HttpStatus.OK);
    }
}
