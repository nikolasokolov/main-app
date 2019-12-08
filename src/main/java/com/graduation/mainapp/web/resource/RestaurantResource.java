package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.model.User;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.web.dto.RestaurantAccountDTO;
import com.graduation.mainapp.web.dto.RestaurantAccountDetails;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/main")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestaurantResource {
    private final RestaurantService restaurantService;

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        log.info("Received request for fetching all restaurants");
        List<Restaurant> restaurants = restaurantService.findAll();
        List<RestaurantDTO> restaurantDTOs = restaurantService.createRestaurantDTOs(restaurants);
        log.info("Finished fetching all restaurants [{}]", restaurants.size());
        return ResponseEntity.accepted().body(restaurantDTOs);
    }

    @RequestMapping(value = "/restaurant/new", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("Received request for saving a new restaurant");
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .build();
        Restaurant savedRestaurant = restaurantService.save(restaurant);
        log.info("Successfully saved new company [{}]", restaurant.getName());
        return ResponseEntity.accepted().body(savedRestaurant);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable Long restaurantId) {
        log.info("Received request for fetching restaurant with ID [{}]", restaurantId);
        Optional<Restaurant> restaurantOptional = restaurantService.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            User user = restaurant.getUser();
            RestaurantAccountDetails restaurantAccountDetails = null;
            if (Objects.nonNull(user)) {
                restaurantAccountDetails = new RestaurantAccountDetails(
                        user.getUsername(), user.getEmail());
            }
            RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                    .id(restaurant.getId())
                    .name(restaurant.getName())
                    .address(restaurant.getAddress())
                    .phoneNumber(restaurant.getPhoneNumber())
                    .logo(restaurant.getLogo())
                    .restaurantAccountDetails(restaurantAccountDetails)
                    .build();
            log.info("Successfully fetched restaurant with ID [{}]", restaurantId);
            return ResponseEntity.accepted().body(restaurantDTO);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/restaurant/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> edit(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("Received request for editing restaurant [{}]", restaurantDTO.getName());
        Optional<Restaurant> restaurantFromDatabase = restaurantService.findById(restaurantDTO.getId());
        Restaurant restaurant = Restaurant.builder()
                .id(restaurantDTO.getId())
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .logo(restaurantFromDatabase.get().getLogo())
                .user(restaurantFromDatabase.get().getUser())
                .build();
        restaurantService.save(restaurant);
        log.info("Successfully updated restaurant [{}]", restaurant.getName());
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/restaurant/{restaurantId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("restaurantId") Long restaurantId, @RequestParam("file") MultipartFile logo) {
        log.info("Received request for uploading logo for restaurant with ID [{}]", restaurantId);
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (!logo.isEmpty()) {
            try {
                restaurantService.saveLogo(restaurant.get(), logo);
                log.info("Successfully uploaded logo for restaurant with ID [{}]", restaurantId);
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with id " + restaurantId);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long restaurantId) {
        log.info("Received request for deleting restaurant with ID [{}]", restaurantId);
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            log.info("Successfully deleted restaurant with ID [{}]", restaurantId);
            restaurantService.delete(restaurant.get());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            log.warn("Restaurant with ID [{}] is not found", restaurantId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/account/add", method = RequestMethod.POST)
    public ResponseEntity<?> addAccountForRestaurant(@PathVariable Long restaurantId, @RequestBody RestaurantAccountDTO restaurantAccountDTO) throws Exception {
        log.info("Received request for creating account for restaurant with ID [{}]", restaurantId);
        Restaurant restaurantWithAccount = restaurantService.addAccountForRestaurant(restaurantId, restaurantAccountDTO);
        if (Objects.nonNull(restaurantWithAccount)) {
            RestaurantAccountDTO restaurantAccountResponseDTO = RestaurantAccountDTO.builder()
                    .username(restaurantAccountDTO.getUsername())
                    .email(restaurantAccountDTO.getEmail())
                    .build();
            log.info("Successfully created account for restaurant with ID [{}]", restaurantId);
            return ResponseEntity.accepted().body(restaurantAccountResponseDTO);
        } else {
            log.info("An error occurred trying to create account for restaurant with ID [{}]", restaurantId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
