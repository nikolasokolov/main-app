package com.graduation.mainapp.service;

import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.web.dto.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<Restaurant> findAll();
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long restaurantId);
    void saveLogo(Restaurant restaurant, MultipartFile logo) throws Exception;
    void delete(Restaurant restaurant);
    List<RestaurantDTO> createRestaurantDTOs(List<Restaurant> restaurants);
}
