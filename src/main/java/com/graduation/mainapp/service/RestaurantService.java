package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.dto.CompanyDTO;
import com.graduation.mainapp.dto.RestaurantAccountDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    List<Restaurant> findAll();

    Restaurant save(Restaurant restaurant);

    Restaurant saveLogo(Long restaurantId, MultipartFile logo) throws Exception;

    boolean delete(Long restaurantId) throws DomainObjectNotFoundException;

    List<RestaurantDTO> createRestaurantDTOs(Collection<Restaurant> restaurants);

    Restaurant addAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception;

    Restaurant createRestaurantObjectFromRestaurantDTO(RestaurantDTO restaurantDTO);

    RestaurantDTO getRestaurantAccountIfPresent(Long restaurantId) throws DomainObjectNotFoundException;

    Restaurant updateRestaurant(RestaurantDTO restaurantDTO) throws DomainObjectNotFoundException;

    Restaurant findByIdOrThrow(Long restaurantId) throws DomainObjectNotFoundException;

    boolean addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId) throws DomainObjectNotFoundException;

    boolean deleteRestaurantForCompany(Long companyId, Long restaurantId) throws DomainObjectNotFoundException;

    List<RestaurantDTO> getRestaurantsForCompany(Long companyId) throws DomainObjectNotFoundException;
}
