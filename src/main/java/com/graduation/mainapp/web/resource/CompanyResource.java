package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/main")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompanyResource {
    private final CompanyService companyService;

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
        Optional<Company> company = companyService.findById(companyId);
        if (company.isPresent()) {
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .id(company.get().getId())
                    .name(company.get().getName())
                    .address(company.get().getAddress())
                    .phoneNumber(company.get().getPhoneNumber())
                    .build();
            log.info("Successfully fetched company with ID [{}]", companyId);
            return ResponseEntity.accepted().body(companyDTO);
        } else {
            log.warn("Company with ID [{}] is not found", companyId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> edit(@RequestBody CompanyDTO companyDTO) {
        log.info("Received request for editing company [{}]", companyDTO.getName());
        Optional<Company> companyFromDatabase = companyService.findById(companyDTO.getId());
        Company company = Company.builder()
                .id(companyDTO.getId())
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .logo(companyFromDatabase.get().getLogo())
                .build();
        companyService.save(company);
        log.info("Successfully updated company [{}]", company.getName());
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/company/{companyId}/uploadLogo", method = RequestMethod.POST)
    public ResponseEntity<?> uploadLogo(@PathVariable("companyId") Long companyId, @RequestParam("file") MultipartFile logo) {
        log.info("Received request for uploading logo for company with ID [{}]", companyId);
        Optional<Company> company = companyService.findById(companyId);
        if (!logo.isEmpty()) {
            try {
                companyService.saveLogo(company.get(), logo);
                log.info("Successfully uploaded logo for company with ID [{}]", companyId);
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with ID [{}]", companyId);
            }
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/company/{companyId}/logo", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("companyId") Long companyId) {
        log.info("Received request for fetching logo for company with ID [{}]", companyId);
        Optional<Company> company = companyService.findById(companyId);
        byte[] imageContent = company.get().getLogo();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        log.info("Successfully fetched logo for company with ID [{}]", companyId);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
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
}
