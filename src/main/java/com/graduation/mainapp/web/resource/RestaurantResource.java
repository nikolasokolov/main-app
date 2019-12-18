package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.RestaurantAccountDTO;
import com.graduation.mainapp.web.dto.RestaurantDTO;
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

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/main")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestaurantResource {
    private final RestaurantService restaurantService;
    private final UserService userService;

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> findAllRestaurants() {
        log.info("Received request for fetching all restaurants");
        List<Restaurant> restaurants = restaurantService.findAll();
        List<RestaurantDTO> restaurantDTOs = restaurantService.createRestaurantDTOs(restaurants);
        log.info("Finished fetching all restaurants [{}]", restaurants.size());
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/restaurant/new", method = RequestMethod.POST)
    public ResponseEntity<?> saveRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("Received request for saving a new restaurant");
        Restaurant restaurant = restaurantService.createRestaurantObjectFromRestaurantDTO(restaurantDTO);
        Restaurant savedRestaurant = restaurantService.save(restaurant);
        log.info("Successfully saved new company [{}]", restaurant.getName());
        return ResponseEntity.ok().body(savedRestaurant);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}", method = RequestMethod.GET)
    public ResponseEntity<?> findRestaurantById(@PathVariable Long restaurantId) {
        log.info("Received request for fetching restaurant with ID [{}]", restaurantId);
        RestaurantDTO restaurantDTO = restaurantService.getRestaurantAccountIfPresent(restaurantId);
        if (Objects.nonNull(restaurantDTO)) {
            log.info("Successfully fetched restaurant with ID [{}]", restaurantId);
            return ResponseEntity.ok().body(restaurantDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/restaurant/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("Received request for editing restaurant [{}]", restaurantDTO.getName());
        Restaurant restaurantForUpdate = restaurantService.updateRestaurant(restaurantDTO);
        if (Objects.nonNull(restaurantForUpdate)) {
            log.info("Successfully updated restaurant [{}]", restaurantForUpdate.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/restaurant/{restaurantId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("restaurantId") Long restaurantId,
                                        @RequestParam("file") MultipartFile logo) throws Exception {
        log.info("Received request for uploading logo for restaurant with ID [{}]", restaurantId);
        Restaurant restaurant = restaurantService.saveLogo(restaurantId, logo);
        if (Objects.nonNull(restaurant.getLogo())) {
            log.info("Successfully uploaded logo for restaurant with ID [{}]", restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long restaurantId) {
        log.info("Received request for deleting restaurant with ID [{}]", restaurantId);
        boolean restaurantIsDeleted = restaurantService.delete(restaurantId);
        if (restaurantIsDeleted) {
            log.info("Successfully deleted restaurant with ID [{}]", restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/account/add", method = RequestMethod.POST)
    public ResponseEntity<?> addAccountForRestaurant(
            @PathVariable Long restaurantId, @RequestBody RestaurantAccountDTO restaurantAccountDTO) throws Exception {
        log.info("Received request for creating account for restaurant with ID [{}]", restaurantId);
        Restaurant restaurant = restaurantService.addAccountForRestaurant(restaurantId, restaurantAccountDTO);
        if (Objects.nonNull(restaurant)) {
            RestaurantAccountDTO restaurantAccountResponseDTO = RestaurantAccountDTO.builder()
                    .username(restaurantAccountDTO.getUsername())
                    .email(restaurantAccountDTO.getEmail())
                    .build();
            log.info("Successfully created account for restaurant with ID [{}]", restaurantId);
            return ResponseEntity.ok().body(restaurantAccountResponseDTO);
        } else {
            log.info("An error occurred trying to create account for restaurant with ID [{}]", restaurantId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/{userId}/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> getRestaurantsForUser(@PathVariable Long userId) throws Exception {
        log.info("Received request for fetching restaurants for user with ID [{}]", userId);
        List<Restaurant> restaurants = userService.getRestaurantsForUser(userId);
        List<RestaurantDTO> restaurantDTOs = restaurantService.createRestaurantDTOs(restaurants);
        log.info("Finished fetching restaurants for user with ID [{}]", userId);
        return new ResponseEntity<>(restaurantDTOs, HttpStatus.OK);
    }
}
