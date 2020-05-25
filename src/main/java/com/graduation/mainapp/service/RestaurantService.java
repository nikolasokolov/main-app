package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import com.graduation.mainapp.rest.dto.RestaurantAccountDTO;
import com.graduation.mainapp.rest.dto.RestaurantDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();

    Restaurant save(Restaurant restaurant);

    void saveLogo(Long restaurantId, MultipartFile logo) throws Exception;

    void deleteRestaurant(Long restaurantId) throws NotFoundException;

    void createAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception;

    void updateRestaurant(RestaurantDTO restaurantDTO) throws NotFoundException;

    Restaurant getRestaurant(Long restaurantId) throws NotFoundException;

    Restaurant findByUser(User user);
}
