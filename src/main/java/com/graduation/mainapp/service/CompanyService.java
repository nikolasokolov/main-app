package com.graduation.mainapp.service;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.web.dto.CompanyDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> findAll();

    Company save(Company company);

    Optional<Company> findById(Long companyId);

    void saveLogo(Company company, MultipartFile logo) throws Exception;

    void delete(Company company);

    List<CompanyDTO> createCompanyDTOs(List<Company> companies);

    void addRestaurantForCompany(Company company, Long restaurantId);

    Company createCompanyObjectFromCompanyDTO(CompanyDTO companyDTO);

    CompanyDTO createCompanyDTOFromCompanyObject(Company company);

    Company createCompanyObjectForUpdate(Company company, CompanyDTO companyDTO);
}
