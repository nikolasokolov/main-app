package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Authority;
import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.model.User;
import com.graduation.mainapp.repository.RestaurantRepository;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.RestaurantAccountDTO;
import com.graduation.mainapp.web.dto.RestaurantAccountDetails;
import com.graduation.mainapp.web.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.graduation.mainapp.util.LogoValidationUtil.validateLogoFormat;

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
    public Restaurant saveLogo(Long restaurantId, MultipartFile logo) throws Exception {
        Optional<Restaurant> optionalRestaurant = this.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            if (!logo.isEmpty()) {
                try {
                    restaurant.setLogo(logo.getBytes());
                } catch (IOException e) {
                    log.error("IOException caught on saveLogo company:  " + restaurant.getName() + "message" + e.getMessage());
                } catch (Exception exception) {
                    log.error("Error while trying to save logo for company with id " + restaurantId);
                }
                validateLogoFormat(logo);
                return this.save(restaurant);
            } else {
                log.error("Logo is not present");
                throw new Exception("Logo is not present");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
    }

    @Override
    @Transactional
    public boolean delete(Long restaurantId) {
        Optional<Restaurant> optionalRestaurant = this.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            Company[] companies = new Company[restaurant.getCompanies().size()];
            companies = restaurant.getCompanies().toArray(companies);
            for (Company company : companies) {
                company.removeRestaurant(restaurant);
            }
            restaurantRepository.delete(restaurant);
            User user = restaurant.getUser();
            if (Objects.nonNull(user)) {
                userService.delete(user);
            }
            return true;
        } else {
            log.error("Restaurant with ID [{}] is not found", restaurantId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
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
    public Restaurant addAccountForRestaurant(Long restaurantId, RestaurantAccountDTO restaurantAccountDTO)
            throws Exception {
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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
            }
        } else {
            throw new Exception("Passwords don't match");
        }
    }

    @Override
    public Restaurant createRestaurantObjectFromRestaurantDTO(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .phoneNumber(restaurantDTO.getPhoneNumber())
                .build();
    }

    @Override
    public RestaurantDTO getRestaurantAccountIfPresent(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = this.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            User user = restaurant.getUser();
            RestaurantAccountDetails restaurantAccountDetails = null;
            if (Objects.nonNull(user)) {
                restaurantAccountDetails = new RestaurantAccountDetails(
                        user.getUsername(), user.getEmail());
            }
            return this.createRestaurantDTOFromRestaurantObject(restaurant, restaurantAccountDetails);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
    }

    @Override
    public Restaurant updateRestaurant(RestaurantDTO restaurantDTO) {
        Optional<Restaurant> optionalRestaurant = this.findById(restaurantDTO.getId());
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            Restaurant restaurantForUpdate = createRestaurantForUpdate(restaurant, restaurantDTO);
            return this.save(restaurantForUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
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

    private RestaurantDTO createRestaurantDTOFromRestaurantObject(
            Restaurant restaurant, RestaurantAccountDetails restaurantAccountDetails) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .phoneNumber(restaurant.getPhoneNumber())
                .logo(restaurant.getLogo())
                .restaurantAccountDetails(restaurantAccountDetails)
                .build();
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
