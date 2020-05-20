package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.CompanyDTO;
import com.graduation.mainapp.dto.RestaurantAccountDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import com.graduation.mainapp.exception.InvalidLogoException;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.MenuItemRepository;
import com.graduation.mainapp.repository.RestaurantRepository;
import com.graduation.mainapp.repository.dao.AvailableCompaniesRestaurantsDAO;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.graduation.mainapp.util.LogoValidationUtil.validateLogoFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    public static final String ROLE_RESTAURANT = "ROLE_RESTAURANT";

    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MenuItemRepository menuItemRepository;
    private final CompanyService companyService;
    private final AvailableCompaniesRestaurantsDAO availableCompaniesRestaurantsDAO;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void saveLogo(Long restaurantId, MultipartFile logo) throws Exception {
        Restaurant restaurant = getRestaurant(restaurantId);
        if (!logo.isEmpty()) {
            try {
                restaurant.setLogo(logo.getBytes());
            } catch (IOException e) {
                log.error("IOException caught on saveLogo company:  " + restaurant.getName() + "message" + e.getMessage());
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with id " + restaurantId);
            }
            validateLogoFormat(logo);
            save(restaurant);
        } else {
            log.error("Logo is not present");
            throw new InvalidLogoException("Logo is not present");
        }
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
        boolean passwordsMatch = checkIfPasswordsMatch(restaurantAccountDTO);
        if (passwordsMatch) {
            User user = createUserForRestaurant(restaurantAccountDTO);
            User savedUser = userService.save(user);
            Restaurant restaurant = getRestaurant(restaurantId);
            restaurant.setUser(savedUser);
            restaurantRepository.save(restaurant);
        } else {
            throw new Exception("Passwords don't match");
        }
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

    private User createUserForRestaurant(RestaurantAccountDTO restaurantAccountDTO) {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(ROLE_RESTAURANT));
        return User.builder()
                .username(restaurantAccountDTO.getUsername())
                .email(restaurantAccountDTO.getEmail())
                .password(passwordEncoder.encode(restaurantAccountDTO.getPassword()))
                .authorities(authorities)
                .build();
    }

    @Override
    @Transactional
    public Set<Restaurant> getRestaurantsForCompany(Long companyId) throws NotFoundException {
        Company company = companyService.getCompany(companyId);
        return company.getRestaurants();
    }

    @Override
    public List<AvailableRestaurantsRowMapper> getAvailableRestaurantsForCompany(Long companyId) {
        return availableCompaniesRestaurantsDAO.getAvailableRestaurantsForCompany(companyId);
    }

    @Override
    public Restaurant findByUser(User user) {
        return restaurantRepository.findByUser(user);
    }

    @Override
    public List<CompanyRowMapper> getCompaniesForRestaurant(Long userId) throws NotFoundException {
        User user = userService.getUser(userId);
        return availableCompaniesRestaurantsDAO.getCompaniesForRestaurant(user.getRestaurant().getId());
    }

    @Override
    @Transactional
    public void addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId) throws NotFoundException {
        Company company = companyService.getCompany(companyDTO.getId());
        Restaurant restaurant = getRestaurant(restaurantId);
        company.getRestaurants().add(restaurant);
        restaurant.getCompanies().add(company);
        companyService.save(company);
        save(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurantForCompany(Long companyId, Long restaurantId) throws NotFoundException {
        Company company = companyService.getCompany(companyId);
        Restaurant restaurant = getRestaurant(restaurantId);
        restaurant.removeCompany(company);
        company.removeRestaurant(restaurant);
        companyService.save(company);
        save(restaurant);
    }

    private boolean checkIfPasswordsMatch(RestaurantAccountDTO restaurantAccountDTO) {
        return restaurantAccountDTO.getPassword().equals(restaurantAccountDTO.getConfirmPassword());
    }
}
