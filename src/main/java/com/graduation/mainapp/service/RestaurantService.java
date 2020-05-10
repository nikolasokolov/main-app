package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.CompanyDTO;
import com.graduation.mainapp.dto.RestaurantAccountDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface RestaurantService {
    List<Restaurant> findAll();

    Restaurant save(Restaurant restaurant);

    Restaurant saveLogo(Long restaurantId, MultipartFile logo) throws Exception;

    boolean delete(Long restaurantId) throws NotFoundException;

    List<RestaurantDTO> createRestaurantDTOs(Collection<Restaurant> restaurants);

    Restaurant addAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception;

    Restaurant createRestaurantObjectFromRestaurantDTO(RestaurantDTO restaurantDTO);

    RestaurantDTO getRestaurantAccountIfPresent(Long restaurantId) throws NotFoundException;

    Restaurant updateRestaurant(RestaurantDTO restaurantDTO) throws NotFoundException;

    Restaurant findByIdOrThrow(Long restaurantId) throws NotFoundException;

    boolean addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId) throws NotFoundException;

    boolean deleteRestaurantForCompany(Long companyId, Long restaurantId) throws NotFoundException;

    List<RestaurantDTO> getRestaurantsForCompany(Long companyId) throws NotFoundException;

    List<RestaurantDTO> getAvailableRestaurantsForCompany(Long companyId);

    Restaurant findByUser(User user);

    List<CompanyRowMapper> getCompaniesForRestaurant(Long userId) throws NotFoundException;
}
