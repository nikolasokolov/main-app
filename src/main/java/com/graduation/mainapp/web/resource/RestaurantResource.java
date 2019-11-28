package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import com.graduation.mainapp.web.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/main")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestaurantResource {
    private final RestaurantService restaurantService;

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<Restaurant>> findAll() {
        List<Restaurant> restaurants = restaurantService.findAll();
        return ResponseEntity.accepted().body(restaurants);
    }

    @RequestMapping(value = "/restaurant/new", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody RestaurantDTO restaurantDTO) {
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .build();
        Restaurant savedRestaurant = restaurantService.save(restaurant);
        return ResponseEntity.accepted().body(savedRestaurant);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                    .id(restaurant.get().getId())
                    .name(restaurant.get().getName())
                    .address(restaurant.get().getAddress())
                    .phoneNumber(restaurant.get().getPhoneNumber())
                    .build();
            return ResponseEntity.accepted().body(restaurantDTO);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/restaurant/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> edit(@RequestBody RestaurantDTO restaurantDTO) {
        Optional<Restaurant> restaurantFromDatabase = restaurantService.findById(restaurantDTO.getId());
        Restaurant restaurant = Restaurant.builder()
                .id(restaurantDTO.getId())
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .logo(restaurantFromDatabase.get().getLogo())
                .build();
        restaurantService.save(restaurant);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/restaurant/{restaurantId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("restaurantId") Long restaurantId, @RequestParam("file") MultipartFile logo) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (!logo.isEmpty()) {
            try {
                restaurantService.saveLogo(restaurant.get(), logo);
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with id " + restaurantId);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/logo", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("restaurantId") Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        byte[] imageContent = restaurant.get().getLogo();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/restaurant/{restaurantId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            restaurantService.delete(restaurant.get());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
