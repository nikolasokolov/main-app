package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.CompanyConverter;
import com.graduation.mainapp.converter.RestaurantConverter;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.dao.rowmapper.AvailableRestaurantsRowMapper;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyRowMapper;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.dto.CompanyDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import com.graduation.mainapp.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main/companies")
public class CompanyResource {

    private final CompanyService companyService;
    private final RestaurantService restaurantService;
    private final CompanyConverter companyConverter;
    private final RestaurantConverter restaurantConverter;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        log.info("Started fetching all companies");
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyDTO> companyDTOs = companyConverter.convertToCompanyDTOs(companies);
        log.info("Finished fetching all companies");
        return ResponseEntity.ok().body(companyDTOs);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<?> createNewCompany(@RequestBody CompanyDTO companyDTO) {
        log.info("Started creating a new Company with name=[{}]", companyDTO.getName());
        Company company = companyConverter.convertToCompany(companyDTO);
        companyService.save(company);
        log.info("Finished creating a new Company with name=[{}]", companyDTO.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long companyId) throws NotFoundException {
        log.info("Started fetching company with ID=[{}]", companyId);
        Company company = companyService.getCompany(companyId);
        CompanyDTO companyDTO = companyConverter.convertToCompanyDTO(company);
        log.info("Finished fetching company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(companyDTO);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDTO companyDTO) throws NotFoundException {
        log.info("Started updating company with name=[{}]", companyDTO.getName());
        companyService.updateCompany(companyDTO);
        log.info("Finished updating company with name=[{}]", companyDTO.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/{companyId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogoForCompany(@PathVariable("companyId") Long companyId, @RequestParam("file") MultipartFile logo) throws Exception {
        log.info("Started uploading logo for Company with ID=[{}]", companyId);
        companyService.saveLogo(companyId, logo);
        log.info("Finished uploading logo for Company with ID=[{}]", companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{companyId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) throws NotFoundException {
        log.info("Started deleting Company with ID=[{}]", companyId);
        companyService.delete(companyId);
        log.info("Finished deleting Company with ID=[{}]", companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/add-restaurant/{restaurantId}", method = RequestMethod.POST)
    public ResponseEntity<?> addRestaurantForCompany(@RequestBody CompanyDTO companyDTO, @PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started adding Restaurant to Company with ID=[{}]", companyDTO.getId());
        restaurantService.addRestaurantForCompany(companyDTO, restaurantId);
        log.info("Finished adding Restaurant to Company with ID=[{}]", companyDTO.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{companyId}/restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsForCompany(@PathVariable Long companyId) throws NotFoundException {
        log.info("Started fetching restaurants for Company with ID=[{}]", companyId);
        Set<Restaurant> restaurants = restaurantService.getRestaurantsForCompany(companyId);
        List<RestaurantDTO> restaurantDTOs = restaurantConverter.convertToRestaurantDTOs(restaurants);
        log.info("Finished fetching restaurants for Company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/{companyId}/delete-restaurant/{restaurantId}")
    public ResponseEntity<?> deleteRestaurantForCompany(@PathVariable Long companyId, @PathVariable Long restaurantId) throws NotFoundException {
        log.info("Started deleting Restaurant with ID=[{}] to Company with ID=[{}]", restaurantId, companyId);
        restaurantService.deleteRestaurantForCompany(companyId, restaurantId);
        log.info("Finished deleting Restaurant with ID=[{}] to Company with ID=[{}]", restaurantId, companyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{companyId}/available-restaurants", method = RequestMethod.GET)
    public ResponseEntity<List<RestaurantDTO>> getAvailableRestaurantsForCompany(@PathVariable Long companyId) {
        log.info("Started fetching available Restaurants for Company with ID=[{}]", companyId);
        List<AvailableRestaurantsRowMapper> availableRestaurantsForCompany = restaurantService.getAvailableRestaurantsForCompany(companyId);
        List<RestaurantDTO> restaurantDTOs = restaurantConverter.convertToAvailableRestaurantDTOs(availableRestaurantsForCompany);
        log.info("Finished fetching available Restaurants for Company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/users/{userId}/restaurant", method = RequestMethod.GET)
    public ResponseEntity<List<CompanyRowMapper>> getCompaniesForRestaurant(@PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching Companies for Restaurant with User with ID=[{}]", userId);
        List<CompanyRowMapper> companyDTOs = restaurantService.getCompaniesForRestaurant(userId);
        log.info("Started fetching Companies for Restaurant with User with ID=[{}]", userId);
        return ResponseEntity.ok().body(companyDTOs);
    }
}
