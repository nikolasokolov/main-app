package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Company>> findAll() {
        List<Company> companies = companyService.findAll();
        return ResponseEntity.accepted().body(companies);
    }

    @RequestMapping(value = "/company/new", method = RequestMethod.POST)
    public ResponseEntity<?> findAll(@RequestBody CompanyDTO companyDTO) {
        Company company = Company.builder()
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .build();
        companyService.save(company);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable Long companyId) {
        Optional<Company> company = companyService.findById(companyId);
        if (company.isPresent()) {
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .name(company.get().getName())
                    .address(company.get().getAddress())
                    .phoneNumber(company.get().getPhoneNumber())
                    .build();
            return ResponseEntity.accepted().body(companyDTO);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
