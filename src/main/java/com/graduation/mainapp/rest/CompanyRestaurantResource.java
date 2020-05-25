package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.RestaurantConverter;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import com.graduation.mainapp.rest.dto.RestaurantDTO;
import com.graduation.mainapp.service.CompanyRestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main/companies")
public class CompanyRestaurantResource {

    private final RestaurantConverter restaurantConverter;
    private final CompanyRestaurantService companyRestaurantService;

    @RequestMapping(value = "/{companyId}/restaurants/{restaurantId}/add", method = RequestMethod.POST)
    public ResponseEntity<?> addRestaurantForCompany(@RequestBody CompanyDTO companyDTO, @PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started adding Restaurant to Company with ID=[{}]", companyDTO.getId());
        companyRestaurantService.addRestaurantForCompany(companyDTO, restaurantId);
        log.info("Finished adding Restaurant to Company with ID=[{}]", companyDTO.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{companyId}/restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsForCompany(@PathVariable Long companyId) throws NotFoundException {
        log.info("Started fetching restaurants for Company with ID=[{}]", companyId);
        Set<Restaurant> restaurants = companyRestaurantService.getRestaurantsForCompany(companyId);
        List<RestaurantDTO> restaurantDTOs = restaurantConverter.convertToRestaurantDTOs(restaurants);
        log.info("Finished fetching restaurants for Company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/{companyId}/restaurants/{restaurantId}/delete")
    public ResponseEntity<?> deleteRestaurantForCompany(@PathVariable Long companyId, @PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started deleting Restaurant with ID=[{}] to Company with ID=[{}]", restaurantId, companyId);
        companyRestaurantService.deleteRestaurantForCompany(companyId, restaurantId);
        log.info("Finished deleting Restaurant with ID=[{}] to Company with ID=[{}]", restaurantId, companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{companyId}/available-restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDTO>> getAvailableRestaurantsForCompany(@PathVariable Long companyId) {
        log.info("Started fetching available Restaurants for Company with ID=[{}]", companyId);
        List<AvailableRestaurantsRowMapper> availableRestaurantsForCompany = companyRestaurantService.getAvailableRestaurantsForCompany(companyId);
        List<RestaurantDTO> restaurantDTOs = restaurantConverter.convertToAvailableRestaurantDTOs(availableRestaurantsForCompany);
        log.info("Finished fetching available Restaurants for Company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<CompanyRowMapper>> getCompaniesForRestaurant(@PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching Companies for Restaurant with User with ID=[{}]", userId);
        List<CompanyRowMapper> companyDTOs = companyRestaurantService.getCompaniesForRestaurant(userId);
        log.info("Started fetching Companies for Restaurant with User with ID=[{}]", userId);
        return ResponseEntity.ok().body(companyDTOs);
    }
}
