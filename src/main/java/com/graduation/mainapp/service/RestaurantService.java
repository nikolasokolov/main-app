package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.dto.RestaurantAccountDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<Restaurant> findAll();

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long restaurantId);

    Restaurant saveLogo(Long restaurantId, MultipartFile logo) throws Exception;

    boolean delete(Long restaurantId);

    List<RestaurantDTO> createRestaurantDTOs(Collection<Restaurant> restaurants);

    Restaurant addAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception;

    Restaurant createRestaurantObjectFromRestaurantDTO(RestaurantDTO restaurantDTO);

    RestaurantDTO getRestaurantAccountIfPresent(Long restaurantId);

    Restaurant updateRestaurant(RestaurantDTO restaurantDTO);
}
