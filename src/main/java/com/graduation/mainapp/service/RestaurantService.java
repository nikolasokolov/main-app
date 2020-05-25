package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.rest.dto.RestaurantAccountDTO;
import com.graduation.mainapp.rest.dto.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();

    Restaurant save(Restaurant restaurant);

    void saveLogo(Long restaurantId, MultipartFile logo) throws Exception;

    void deleteRestaurant(Long restaurantId) throws NotFoundException;

    void createAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception;

    void updateRestaurant(RestaurantDTO restaurantDTO) throws NotFoundException;

    Restaurant getRestaurant(Long restaurantId) throws NotFoundException;
}
