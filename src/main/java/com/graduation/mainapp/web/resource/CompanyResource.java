package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.model.Restaurant;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import com.graduation.mainapp.web.dto.RestaurantDTO;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/main")
@Slf4j
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
        return ResponseEntity.accepted().body(companyDTOs);
    }

    @RequestMapping(value = "/company/new", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody CompanyDTO companyDTO) {
        log.info("Received request for saving a new company");
        Company company = Company.builder()
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .build();
        Company savedCompany = companyService.save(company);
        log.info("Successfully saved new company [{}]", company.getName());
        return ResponseEntity.accepted().body(savedCompany);
    }

    @RequestMapping(value = "/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable Long companyId) {
        log.info("Received request for fetching company with ID [{}]", companyId);
        Optional<Company> companyOptional = companyService.findById(companyId);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .address(company.getAddress())
                    .phoneNumber(company.getPhoneNumber())
                    .logo(company.getLogo())
                    .build();
            log.info("Successfully fetched company with ID [{}]", companyId);
            return ResponseEntity.accepted().body(companyDTO);
        } else {
            log.warn("Company with ID [{}] is not found", companyId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody CompanyDTO companyDTO) {
        log.info("Received request for editing company [{}]", companyDTO.getName());
        Optional<Company> companyFromDatabase = companyService.findById(companyDTO.getId());
        if (companyFromDatabase.isPresent()) {
            Company company = companyFromDatabase.get();
            Company companyToBeUpdated = Company.builder()
                    .id(companyDTO.getId())
                    .name(companyDTO.getName())
                    .address(companyDTO.getAddress())
                    .phoneNumber(companyDTO.getPhoneNumber())
                    .logo(company.getLogo())
                    .build();
            companyService.save(companyToBeUpdated);
            log.info("Successfully updated company [{}]", company.getName());
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            log.warn("Company with ID [{}] is not found", companyDTO.getId());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/company/{companyId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("companyId") Long companyId, @RequestParam("file") MultipartFile logo) {
        log.info("Received request for uploading logo for company with ID [{}]", companyId);
        Optional<Company> companyOptional = companyService.findById(companyId);
        if (!logo.isEmpty()) {
            try {
                if (companyOptional.isPresent()) {
                    Company company = companyOptional.get();
                    companyService.saveLogo(company, logo);
                    log.info("Successfully uploaded logo for company with ID [{}]", companyId);
                } else {
                    log.warn("Company with ID [{}] is not found", companyId);
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
                }
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with ID [{}]", companyId);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/company/{companyId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        log.info("Received request for deleting company with ID [{}]", companyId);
        Optional<Company> company = companyService.findById(companyId);
        if (company.isPresent()) {
            companyService.delete(company.get());
            log.info("Successfully deleted company with ID [{}]", companyId);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            log.warn("Company with ID [{}] is not found", companyId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/company/add-restaurant/{restaurantId}", method = RequestMethod.POST)
    public ResponseEntity<?> addRestaurantForCompany(@RequestBody CompanyDTO companyDTO, @PathVariable Long restaurantId) {
        log.info("Received request for adding restaurant to company with ID [{}]", companyDTO.getId());
        Optional<Company> optionalCompany = companyService.findById(companyDTO.getId());
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            companyService.addRestaurantForCompany(company, restaurantId);
            log.info("Successfully added restaurant to company with ID [{}]", companyDTO.getId());
        } else {
            log.warn("Company with ID [{}] is not found", optionalCompany.get().getId());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/company/{companyId}/restaurants", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> getRestaurantsForCompany(@PathVariable Long companyId) {
        Optional<Company> optionalCompany = companyService.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Set<Restaurant> restaurantsForCompany = company.getRestaurants();
            List<RestaurantDTO> restaurantDTOs = restaurantService.createRestaurantDTOs(restaurantsForCompany);
            return ResponseEntity.ok().body(restaurantDTOs);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/company/{companyId}/delete-restaurant/{restaurantId}")
    @Transactional
    public ResponseEntity<?> deleteRestaurantForCompany(@PathVariable Long companyId, @PathVariable Long restaurantId) {
        log.info("Received request for deleting restaurant with ID [{}] to company with ID [{}]", restaurantId, companyId);
        Optional<Company> optionalCompany = companyService.findById(companyId);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();
            Optional<Restaurant> optionalRestaurant = restaurantService.findById(restaurantId);
            if (optionalRestaurant.isPresent()) {
                Restaurant restaurant = optionalRestaurant.get();
                restaurant.removeCompany(company);
                company.removeRestaurant(restaurant);
                companyService.save(company);
                restaurantService.save(restaurant);
                log.info("Successfully deleted restaurant with ID [{}] to company with ID [{}]", restaurantId, companyId);
                return new ResponseEntity(HttpStatus.ACCEPTED);
            } else {
                log.warn("Restaurant with ID [{}] is not found", restaurantId);
            }
        } else {
            log.warn("Company with ID [{}] is not found", companyId);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
