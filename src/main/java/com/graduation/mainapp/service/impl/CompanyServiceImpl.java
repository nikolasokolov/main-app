package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.repository.CompanyRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.RestaurantService;
import com.graduation.mainapp.dto.CompanyDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.graduation.mainapp.util.LogoValidationUtil.validateLogoFormat;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company findByIdOrThrow(Long companyId) throws DomainObjectNotFoundException {
        return companyRepository.findById(companyId).orElseThrow(
                () -> new DomainObjectNotFoundException("Company with ID " + companyId + " is not found"));
    }

    @Override
    public Company saveLogo(Long companyId, MultipartFile logo) throws Exception {
        if (!logo.isEmpty()) {
            Company company = findByIdOrThrow(companyId);
            try {
                company.setLogo(logo.getBytes());
            } catch (IOException e) {
                log.error("IOException caught on saveLogo company:  " + company.getName() + "message" + e.getMessage());
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with ID [{}]", companyId);
            }
            validateLogoFormat(logo);
            return this.save(company);
        } else {
            log.error("Logo not present");
            throw new Exception("Logo not present");
        }
    }

    @Override
    public boolean delete(Long companyId) throws DomainObjectNotFoundException {
        Company company = findByIdOrThrow(companyId);
        companyRepository.delete(company);
        return true;
    }

    @Override
    public List<CompanyDTO> createCompanyDTOs(Collection<Company> companies) {
        return companies.stream().map(company -> {
            byte[] companyLogo = company.getLogo();
            return new CompanyDTO(
                    company.getId(),
                    company.getName(),
                    company.getAddress(),
                    company.getAddress(),
                    companyLogo);
        }).collect(Collectors.toList());
    }

    @Override
    public Company createCompanyObjectFromCompanyDTO(CompanyDTO companyDTO) {
        return Company.builder()
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .build();
    }

    @Override
    public CompanyDTO createCompanyDTOFromCompanyObject(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .phoneNumber(company.getPhoneNumber())
                .logo(company.getLogo())
                .build();
    }

    @Override
    public Company createCompanyObjectForUpdate(Company company, CompanyDTO companyDTO) {
        return Company.builder()
                .id(companyDTO.getId())
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .logo(company.getLogo())
                .restaurants(company.getRestaurants())
                .build();
    }

    @Override
    public Company updateCompany(CompanyDTO companyDTO) throws DomainObjectNotFoundException {
        Company companyFromDatabase = findByIdOrThrow(companyDTO.getId());
        Company companyToBeUpdated = this.createCompanyObjectForUpdate(companyFromDatabase, companyDTO);
        return this.save(companyToBeUpdated);
    }
}
