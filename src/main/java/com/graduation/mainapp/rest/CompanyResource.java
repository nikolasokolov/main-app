package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.CompanyConverter;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import com.graduation.mainapp.service.CompanyService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/main/companies")
public class CompanyResource {

    private final CompanyService companyService;
    private final CompanyConverter companyConverter;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        log.info("Started fetching all companies");
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyDTO> companyDTOs = companyConverter.convertToCompanyDTOs(companies);
        log.info("Finished fetching all companies");
        return ResponseEntity.ok().body(companyDTOs);
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyRequestDTO) {
        log.info("Started creating a new Company with name=[{}]", companyRequestDTO.getName());
        Company company = companyConverter.convertToCompany(companyRequestDTO);
        Company savedCompany = companyService.save(company);
        CompanyDTO companyDTO = companyConverter.convertToCompanyDTO(savedCompany);
        log.info("Finished creating a new Company with name=[{}]", companyRequestDTO.getName());
        return ResponseEntity.ok(companyDTO);
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
}
