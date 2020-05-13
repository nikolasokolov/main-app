package com.graduation.mainapp.converter;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.RestaurantAccountDetails;
import com.graduation.mainapp.dto.RestaurantDTO;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RestaurantConverter {

    public List<RestaurantDTO> convertToRestaurantDTOs(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(restaurant -> RestaurantDTO.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .address(restaurant.getAddress())
                        .phoneNumber(restaurant.getPhoneNumber())
                        .logo(restaurant.getLogo())
                        .build())
                .collect(Collectors.toList());
    }

    public Restaurant convertToRestaurant(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .build();
    }

    public RestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        User user = restaurant.getUser();
        RestaurantAccountDetails restaurantAccountDetails = null;
        if (Objects.nonNull(user)) {
            restaurantAccountDetails = new RestaurantAccountDetails(user.getUsername(), user.getEmail());
        }
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phoneNumber(restaurant.getPhoneNumber())
                .logo(restaurant.getLogo())
                .restaurantAccountDetails(restaurantAccountDetails)
                .build();
    }

    public List<RestaurantDTO> convertToAvailableRestaurantDTOs(Collection<AvailableRestaurantsRowMapper> availableRestaurantsForCompany) {
        return availableRestaurantsForCompany.stream()
                .map(restaurant -> RestaurantDTO.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .address(restaurant.getAddress())
                        .phoneNumber(restaurant.getPhoneNumber())
                        .logo(restaurant.getLogo())
                        .build())
                .collect(Collectors.toList());
    }
}
