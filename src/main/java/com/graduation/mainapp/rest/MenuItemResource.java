package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.MenuItemConverter;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.rest.dto.MenuItemDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main")
public class MenuItemResource {

    private final MenuItemService menuItemService;
    private final MenuItemConverter menuItemConverter;

    @RequestMapping(value = "/restaurant/{userId}/menu-items", method = RequestMethod.GET)
    public ResponseEntity<List<MenuItemDTO>> getRestaurantMenuItems(@PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching Menu Items for Restaurant with User ID [{}]", userId);
        List<MenuItem> menuItems = menuItemService.getRestaurantMenuItems(userId);
        List<MenuItemDTO> menuItemDTO = menuItemConverter.convertToMenuItemDTOs(menuItems);
        log.info("Finished fetching Menu Items for Restaurant with User ID [{}]", userId);
        return new ResponseEntity<>(menuItemDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/menu-items/{menuItemId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long menuItemId) throws NotFoundException {
        log.info("Started  deleting Menu Item with ID [{}]", menuItemId);
        menuItemService.deleteMenuItem(menuItemId);
        log.info("Finished deleting Menu Item for with ID [{}]", menuItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/{userId}/menu-items/add", method = RequestMethod.POST)
    public ResponseEntity<MenuItemDTO> addMenuItem(@PathVariable Long userId, @RequestBody MenuItemDTO menuItemDTO) throws NotFoundException {
        log.info("Started creating Menu Item for Restaurant with User ID=[{}]", userId);
        MenuItem menuItem = menuItemService.createMenuItem(userId, menuItemDTO);
        MenuItemDTO menuItemDTOResponse = menuItemConverter.convertToMenuItemDTO(menuItem);
        log.info("Finished creating Menu Item for Restaurant with User ID=[{}]", userId);
        return ResponseEntity.ok(menuItemDTOResponse);
    }

    @RequestMapping(value = "/restaurant/{userId}/menu-items/update", method = RequestMethod.PUT)
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long userId, @RequestBody MenuItemDTO menuItemDTO) throws NotFoundException {
        log.info("Started updating Menu Item for Restaurant with User ID=[{}]", userId);
        MenuItem menuItem = menuItemService.updateMenuItem(userId, menuItemDTO);
        MenuItemDTO menuItemResponseDTO = menuItemConverter.convertToMenuItemDTO(menuItem);
        log.info("Finished updating Menu Item for Restaurant with User ID=[{}]", userId);
        return ResponseEntity.ok(menuItemResponseDTO);
    }

    @RequestMapping(value = "/restaurant/menu-items/{id}", method = RequestMethod.GET)
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable Long id) throws NotFoundException {
        log.info("Started fetching Menu Item with ID=[{}]", id);
        MenuItem menuItem = menuItemService.getMenuItem(id);
        MenuItemDTO menuItemDTO = menuItemConverter.convertToMenuItemDTO(menuItem);
        log.info("Finished fetching Menu Item with ID=[{}]", id);
        return ResponseEntity.ok(menuItemDTO);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/menu", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<MenuItemDTO>>> getRestaurantMenu(@PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started fetching Menu Items for restaurant with ID=[{}]", restaurantId);
        List<MenuItem> menuItems = menuItemService.getRestaurantMenu(restaurantId);
        Map<String, List<MenuItemDTO>> typeToMenuItemsDTO = menuItemConverter.createTypeToMenuItemsDTO(menuItems);
        log.info("Finished fetching Menu Items for restaurant with ID=[{}]", restaurantId);
        return ResponseEntity.ok(typeToMenuItemsDTO);
    }
}
