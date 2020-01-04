package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.RestaurantAccountDTO;
import com.graduation.mainapp.dto.RestaurantAccountDetails;
import com.graduation.mainapp.dto.RestaurantDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.repository.RestaurantRepository;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private final MenuItemRepository menuItemRepository;

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant saveLogo(Long restaurantId, MultipartFile logo) throws Exception {
        Restaurant restaurant = findByIdOrThrow(restaurantId);
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
    }

    @Override
    @Transactional
    public boolean delete(Long restaurantId) throws DomainObjectNotFoundException {
        Restaurant restaurant = findByIdOrThrow(restaurantId);
        Company[] companies = new Company[restaurant.getCompanies().size()];
        companies = restaurant.getCompanies().toArray(companies);
        for (Company company : companies) {
            company.removeRestaurant(restaurant);
        }
        menuItemRepository.deleteAllByRestaurantId(restaurantId);
        restaurantRepository.delete(restaurant);
        User user = restaurant.getUser();
        if (Objects.nonNull(user)) {
            userService.delete(user);
        }
        return true;
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
            Restaurant restaurant = findByIdOrThrow(restaurantId);
            restaurant.setUser(savedUser);
            return restaurantRepository.save(restaurant);
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
    public RestaurantDTO getRestaurantAccountIfPresent(Long restaurantId) throws DomainObjectNotFoundException {
        Restaurant restaurant = findByIdOrThrow(restaurantId);
        User user = restaurant.getUser();
        RestaurantAccountDetails restaurantAccountDetails = null;
        if (Objects.nonNull(user)) {
            restaurantAccountDetails = new RestaurantAccountDetails(
                    user.getUsername(), user.getEmail());
        }
        return this.createRestaurantDTOFromRestaurantObject(restaurant, restaurantAccountDetails);
    }

    @Override
    public Restaurant updateRestaurant(RestaurantDTO restaurantDTO) throws DomainObjectNotFoundException {
        Restaurant restaurant = findByIdOrThrow(restaurantDTO.getId());
        Restaurant restaurantForUpdate = createRestaurantForUpdate(restaurant, restaurantDTO);
        return this.save(restaurantForUpdate);
    }

    @Override
    public Restaurant findByIdOrThrow(Long restaurantId) throws DomainObjectNotFoundException {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new DomainObjectNotFoundException("Restaurant with ID " + restaurantId + " is not found"));
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
