package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.RestaurantConverter;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.rest.dto.RestaurantAccountDTO;
import com.graduation.mainapp.rest.dto.RestaurantDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main")
public class RestaurantResource {

    private final RestaurantService restaurantService;
    private final UserService userService;
    private final RestaurantConverter restaurantConverter;

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        log.info("Started fetching all restaurants");
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        List<RestaurantDTO> restaurantDTOs = restaurantConverter.convertToRestaurantDTOs(restaurants);
        log.info("Finished fetching all restaurants");
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/restaurant/new", method = RequestMethod.POST)
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("Started saving a new restaurant");
        Restaurant restaurant = restaurantConverter.convertToRestaurant(restaurantDTO);
        Restaurant restaurantToBeSaved = restaurantService.save(restaurant);
        RestaurantDTO restaurantResponseDTO = restaurantConverter.convertToRestaurantDTO(restaurantToBeSaved);
        log.info("Finished saving new restaurant [{}]", restaurant.getName());
        return ResponseEntity.ok(restaurantResponseDTO);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}", method = RequestMethod.GET)
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started fetching restaurant with ID=[{}]", restaurantId);
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        RestaurantDTO restaurantDTO = restaurantConverter.convertToRestaurantDTO(restaurant);
        log.info("Finished fetching restaurant with ID=[{}]", restaurantId);
        return ResponseEntity.ok().body(restaurantDTO);
    }

    @RequestMapping(value = "/restaurant/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRestaurant(@RequestBody RestaurantDTO restaurantDTO) throws NotFoundException {
        log.info("Started updating restaurant with ID=[{}]", restaurantDTO.getId());
        restaurantService.updateRestaurant(restaurantDTO);
        log.info("Finished updating restaurant with ID=[{}]", restaurantDTO.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/restaurant/{restaurantId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("restaurantId") Long restaurantId,
                                        @RequestParam("file") MultipartFile logo) throws Exception {
        log.info("Started uploading logo for restaurant with ID=[{}]", restaurantId);
        restaurantService.saveLogo(restaurantId, logo);
        log.info("Finished uploading logo for restaurant with ID=[{}]", restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started deleting restaurant with ID=[{}]", restaurantId);
        restaurantService.deleteRestaurant(restaurantId);
        log.info("Finished deleting restaurant with ID=[{}]", restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/account/add", method = RequestMethod.POST)
    public ResponseEntity<?> createAccountForRestaurant(@PathVariable Long restaurantId,
                                                     @RequestBody @Valid RestaurantAccountDTO restaurantAccountDTO) throws Exception {
        log.info("Started creating account for restaurant with ID=[{}]", restaurantId);
        restaurantService.createAccountForRestaurant(restaurantId, restaurantAccountDTO);
        log.info("Finished created account for restaurant with ID=[{}]", restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{userId}/restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsForUser(@PathVariable Long userId) throws Exception {
        log.info("Started fetching restaurants for user with ID=[{}]", userId);
        List<Restaurant> restaurants = userService.getRestaurantsForUser(userId);
        List<RestaurantDTO> restaurantDTOs = restaurantConverter.convertToRestaurantDTOs(restaurants);
        log.info("Finished fetching restaurants for user with ID=[{}]", userId);
        return ResponseEntity.ok(restaurantDTOs);
    }
}
