package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Authority;
import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.model.User;
import com.graduation.mainapp.repository.RestaurantRepository;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.RestaurantAccountDTO;
import com.graduation.mainapp.web.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> findById(Long companyId) {
        return restaurantRepository.findById(companyId);
    }

    @Override
    public void saveLogo(Restaurant restaurant, MultipartFile logo) throws Exception {
        try {
            restaurant.setLogo(logo.getBytes());
        } catch (IOException e) {
            log.error("IOException caught on saveLogo company:  " + restaurant.getName() + "message" + e.getMessage());
        }
        String fileName = logo.getOriginalFilename();
        int dotIndex = Objects.requireNonNull(fileName).lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new Exception("Invalid image format");
        }
        save(restaurant);
    }

    @Override
    @Transactional
    public void delete(Restaurant restaurant) {
        restaurant.getCompanies().forEach(restaurant::removeCompany);
        restaurantRepository.delete(restaurant);
        User user = restaurant.getUser();
        if (Objects.nonNull(user)) {
            userService.delete(user);
        }
    }

    @Override
    public List<RestaurantDTO> createRestaurantDTOs(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(restaurant -> {
            byte[] restaurantLogo = restaurant.getLogo();
            return new RestaurantDTO(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getAddress(),
                    restaurant.getAddress(),
                    restaurantLogo,
                    null);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Restaurant addAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO) throws Exception {
        boolean passwordsMatch = checkIfPasswordsMatch(restaurantAccountDTO);
        if (passwordsMatch) {
            User user = createUserForRestaurant(restaurantAccountDTO);
            User savedUser = userService.save(user);
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
            if (restaurantOptional.isPresent()) {
                Restaurant restaurant = restaurantOptional.get();
                restaurant.setUser(savedUser);
                return restaurantRepository.save(restaurant);
            } else {
                throw new Exception("Exception occurred while trying to add account for restaurant");
            }
        } else {
            throw new Exception("Passwords don't match");
        }
    }

    private boolean checkIfPasswordsMatch(RestaurantAccountDTO restaurantAccountDTO) {
        return restaurantAccountDTO.getPassword().equals(restaurantAccountDTO.getConfirmPassword());
    }

    private User createUserForRestaurant(RestaurantAccountDTO restaurantAccountDTO) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority("ROLE_RESTAURANT"));
        return User.builder()
                .username(restaurantAccountDTO.getUsername())
                .email(restaurantAccountDTO.getEmail())
                .password(passwordEncoder.encode(restaurantAccountDTO.getPassword()))
                .authorities(authorities)
                .build();
    }
}
