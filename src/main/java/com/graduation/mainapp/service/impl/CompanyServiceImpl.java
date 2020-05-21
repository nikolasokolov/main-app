package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.exception.InvalidLogoException;
import com.graduation.mainapp.repository.CompanyRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.graduation.mainapp.util.LogoValidationUtil.validateLogoFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getCompany(Long companyId) throws NotFoundException {
        return companyRepository.findById(companyId).orElseThrow(
                () -> new NotFoundException("Company with ID " + companyId + " is not found"));
    }

    @Override
    public Company saveLogo(Long companyId, MultipartFile logo) throws Exception {
        if (!logo.isEmpty()) {
            Company company = getCompany(companyId);
            try {
                company.setLogo(logo.getBytes());
            } catch (IOException e) {
                log.error("IOException caught on saveLogo company:  " + company.getName() + "message" + e.getMessage());
            } catch (Exception exception) {
                log.error("Error while trying to save logo for company with ID [{}]", companyId);
            }
            validateLogoFormat(logo);
            return save(company);
        } else {
            log.error("Logo not present");
            throw new InvalidLogoException("Logo not present");
        }
    }

    @Override
    public boolean delete(Long companyId) throws NotFoundException {
        Company company = getCompany(companyId);
        companyRepository.delete(company);
        return true;
    }

    @Override
    public Company updateCompany(CompanyDTO companyDTO) throws NotFoundException {
        Company companyFromDatabase = getCompany(companyDTO.getId());
        Company companyToBeUpdated = createCompanyObjectForUpdate(companyFromDatabase, companyDTO);
        return save(companyToBeUpdated);
    }

    private Company createCompanyObjectForUpdate(Company company, CompanyDTO companyDTO) {
        return Company.builder()
                .id(companyDTO.getId())
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .logo(company.getLogo())
                .restaurants(company.getRestaurants())
                .build();
    }

}
