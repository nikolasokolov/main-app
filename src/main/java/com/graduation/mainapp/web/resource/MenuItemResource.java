package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.MenuItem;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.web.dto.MenuItemsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
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
        List<MenuItemsDTO> menuItemsDTO = menuItemService.createMenuItemsDTO(menuItems);
        log.info("Finished fetching menu items for restaurant account with ID [{}]", userId);
        return new ResponseEntity<>(menuItemsDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/menu-item/{menuItemId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long menuItemId) {
        log.info("Received request for deleting menu item with ID [{}]", menuItemId);
        menuItemService.delete(menuItemId);
        log.info("Successfully deleted menu item for with ID [{}]", menuItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
