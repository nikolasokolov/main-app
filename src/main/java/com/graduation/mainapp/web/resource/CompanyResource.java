package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import com.graduation.mainapp.web.dto.RestaurantDTO;
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
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseEntity<?> findById(@PathVariable Long companyId) {
        log.info("Received request for fetching company with ID [{}]", companyId);
        Optional<Company> companyOptional = companyService.findById(companyId);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            CompanyDTO companyDTO = companyService.createCompanyDTOFromCompanyObject(company);
            log.info("Successfully fetched company with ID [{}]", companyId);
            return ResponseEntity.ok().body(companyDTO);
        } else {
            log.error("Company with ID [{}] is not found", companyId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody CompanyDTO companyDTO) {
        log.info("Received request for editing company [{}]", companyDTO.getName());
        Optional<Company> companyFromDatabase = companyService.findById(companyDTO.getId());
        if (companyFromDatabase.isPresent()) {
            Company company = companyFromDatabase.get();
            Company companyToBeUpdated = companyService.createCompanyObjectForUpdate(company, companyDTO);
            companyService.save(companyToBeUpdated);
            log.info("Successfully updated company [{}]", company.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Company with ID [{}] is not found", companyDTO.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
    }

    @RequestMapping(path = "/company/{companyId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("companyId") Long companyId, @RequestParam("file") MultipartFile logo) throws Exception {
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
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
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
    public ResponseEntity<?> addRestaurantForCompany(@RequestBody CompanyDTO companyDTO, @PathVariable Long restaurantId) {
        log.info("Received request for adding restaurant to company with ID [{}]", companyDTO.getId());
        boolean restaurantIsAdded = companyService.addRestaurantForCompany(companyDTO, restaurantId);
        if (restaurantIsAdded) {
            log.info("Successfully added restaurant to company with ID [{}]", companyDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/company/{companyId}/restaurants", method = RequestMethod.GET)
    public ResponseEntity<?> getRestaurantsForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching restaurants for company with ID [{}]", companyId);
        List<RestaurantDTO> restaurantDTOs = companyService.getRestaurantsForCompany(companyId);
        log.info("Finished fetching restaurants for company with ID [{}]", companyId);
        return ResponseEntity.ok().body(restaurantDTOs);
    }

    @RequestMapping(value = "/company/{companyId}/delete-restaurant/{restaurantId}")
    public ResponseEntity<?> deleteRestaurantForCompany(@PathVariable Long companyId, @PathVariable Long restaurantId) {
        log.info("Received request for deleting restaurant with ID [{}] to company with ID [{}]", restaurantId, companyId);
        boolean restaurantIsDeleted = companyService.deleteRestaurantForCompany(companyId, restaurantId);
        if (restaurantIsDeleted) {
            log.info("Successfully deleted restaurant with ID [{}] to company with ID [{}]", restaurantId, companyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
