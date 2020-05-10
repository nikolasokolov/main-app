package com.graduation.mainapp.rest;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.exception.NotFoundException;
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

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/main")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompanyResource {
    private final CompanyService companyService;
    private final RestaurantService restaurantService;

    @RequestMapping(value = "/companies", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {
        log.info("Received request for fetching all companies");
        List<Company> companies = companyService.findAll();
        List<CompanyDTO> companyDTOs = companyService.createCompanyDTOs(companies);
        log.info("Finished fetching all companies [{}]", companies.size());
        return ResponseEntity.ok().body(companyDTOs);
    }

    @RequestMapping(value = "/company/new", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody CompanyDTO companyDTO) {
        log.info("Received request for saving a new company");
        Company company = companyService.createCompanyObjectFromCompanyDTO(companyDTO);
        Company savedCompany = companyService.save(company);
        log.info("Successfully saved new company [{}]", company.getName());
        return ResponseEntity.ok().body(savedCompany);
    }

    @RequestMapping(value = "/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable Long companyId) throws NotFoundException {
        log.info("Received request for fetching company with ID [{}]", companyId);
        Company company = companyService.findByIdOrThrow(companyId);
        CompanyDTO companyDTO = companyService.createCompanyDTOFromCompanyObject(company);
        log.info("Successfully fetched company with ID [{}]", companyId);
        return ResponseEntity.ok().body(companyDTO);
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDTO companyDTO) throws NotFoundException {
        log.info("Received request for editing company [{}]", companyDTO.getName());
        Company updatedCompany = companyService.updateCompany(companyDTO);
        if (Objects.nonNull(updatedCompany)) {
            log.info("Successfully updated company [{}]", updatedCompany.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/company/{companyId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogoForCompany(@PathVariable("companyId") Long companyId, @RequestParam("file") MultipartFile logo) throws Exception {
        log.info("Received request for uploading logo for company with ID [{}]", companyId);
        Company company = companyService.saveLogo(companyId, logo);
        if (Objects.nonNull(company.getLogo())) {
            log.info("Successfully uploaded logo for company with ID [{}]", companyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/company/{companyId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) throws NotFoundException {
        log.info("Received request for deleting company with ID [{}]", companyId);
        boolean companyIsDeleted = companyService.delete(companyId);
        if (companyIsDeleted) {
            log.info("Successfully deleted company with ID [{}]", companyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/company/add-restaurant/{restaurantId}", method = RequestMethod.POST)
    public ResponseEntity<?> addRestaurantForCompany(@RequestBody CompanyDTO companyDTO, @PathVariable Long restaurantId) throws NotFoundException {
        log.info("Received request for adding restaurant to company with ID [{}]", companyDTO.getId());
        boolean restaurantIsAdded = restaurantService.addRestaurantForCompany(companyDTO, restaurantId);
        if (restaurantIsAdded) {
            log.info("Successfully added restaurant to company with ID [{}]", companyDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/company/{companyId}/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> getRestaurantsForCompany(@PathVariable Long companyId) throws NotFoundException {
        log.info("Received request for fetching restaurants for company with ID [{}]", companyId);
        List<RestaurantDTO> restaurantDTOs = restaurantService.getRestaurantsForCompany(companyId);
        log.info("Finished fetching restaurants for company with ID [{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/company/{companyId}/delete-restaurant/{restaurantId}")
    public ResponseEntity<?> deleteRestaurantForCompany(@PathVariable Long companyId, @PathVariable Long restaurantId) throws NotFoundException {
        log.info("Received request for deleting restaurant with ID [{}] to company with ID [{}]", restaurantId, companyId);
        boolean restaurantIsDeleted = restaurantService.deleteRestaurantForCompany(companyId, restaurantId);
        if (restaurantIsDeleted) {
            log.info("Successfully deleted restaurant with ID [{}] to company with ID [{}]", restaurantId, companyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/company/{companyId}/available-restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailableRestaurantsForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching available restaurants for company with ID [{}]", companyId);
        List<RestaurantDTO> restaurantDTOs = restaurantService.getAvailableRestaurantsForCompany(companyId);
        log.info("Finished fetching available restaurants for company with ID [{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/restaurant/{userId}/companies", method = RequestMethod.GET)
    public ResponseEntity<?> getCompaniesForRestaurant(@PathVariable Long userId) throws NotFoundException {
        log.info("Started fetching companies for restaurant with user ID [{}]", userId);
        List<CompanyRowMapper> companyDTOs = restaurantService.getCompaniesForRestaurant(userId);
        log.info("Finished fetching companies for restaurant with user ID [{}]", userId);
        return ResponseEntity.ok().body(companyDTOs);
    }
}
