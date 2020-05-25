package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.converter.UserConverter;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.InvalidLogoException;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.repository.RestaurantRepository;
import com.graduation.mainapp.rest.dto.RestaurantAccountDTO;
import com.graduation.mainapp.rest.dto.RestaurantDTO;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.graduation.mainapp.util.LogoValidationUtil.validateLogoFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MenuItemRepository menuItemRepository;
    private final UserConverter userConverter;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void saveLogo(Long restaurantId, MultipartFile logo) throws NotFoundException, InvalidLogoException {
        Restaurant restaurant = getRestaurant(restaurantId);
        try {
            restaurant.setLogo(logo.getBytes());
        } catch (IOException e) {
            log.error("IOException caught on saveLogo company:  " + restaurant.getName() + "message" + e.getMessage());
        }
        validateLogoFormat(logo);
        save(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurant(Long restaurantId) throws NotFoundException {
        Restaurant restaurant = getRestaurant(restaurantId);
        Company[] companies = new Company[restaurant.getCompanies().size()];
        companies = restaurant.getCompanies().toArray(companies);
        for (Company company : companies) {
            company.removeRestaurant(restaurant);
        }
        menuItemRepository.deleteAllByRestaurantId(restaurantId);
        restaurantRepository.delete(restaurant);
        User user = restaurant.getUser();
        if (Objects.nonNull(user)) {
            userService.deleteUser(user.getId());
        }
    }

    @Override
    @Transactional
    public void createAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception {
        String password = passwordEncoder.encode(restaurantAccountDTO.getPassword());
        User user = userConverter.convertToUser(restaurantAccountDTO, password);
        User savedUser = userService.save(user);
        Restaurant restaurant = getRestaurant(restaurantId);
        restaurant.setUser(savedUser);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void updateRestaurant(RestaurantDTO restaurantDTO) throws NotFoundException {
        Restaurant restaurant = getRestaurant(restaurantDTO.getId());
        Restaurant restaurantForUpdate = createRestaurantForUpdate(restaurant, restaurantDTO);
        save(restaurantForUpdate);
    }

    @Override
    public Restaurant getRestaurant(Long restaurantId) throws NotFoundException {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant with ID=[" + restaurantId + "] is not found"));
    }

    @Override
    public Restaurant findByUser(User user) {
        return restaurantRepository.findByUser(user);
    }

    private Restaurant createRestaurantForUpdate(Restaurant restaurant, RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .id(restaurantDTO.getId())
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .logo(restaurant.getLogo())
                .user(restaurant.getUser())
                .build();
    }
}
