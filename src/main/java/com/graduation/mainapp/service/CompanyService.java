package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.dto.CompanyDTO;
import com.graduation.mainapp.dto.RestaurantDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> findAll();

    Company save(Company company);

    Optional<Company> findById(Long companyId);

    Company saveLogo(Long companyId, MultipartFile logo) throws Exception;

    boolean delete(Long companyId);

    List<CompanyDTO> createCompanyDTOs(Collection<Company> companies);

    boolean addRestaurantForCompany(CompanyDTO companyDTO, Long restaurantId);

    Company createCompanyObjectFromCompanyDTO(CompanyDTO companyDTO);

    CompanyDTO createCompanyDTOFromCompanyObject(Company company);

    Company createCompanyObjectForUpdate(Company company, CompanyDTO companyDTO);

    boolean deleteRestaurantForCompany(Long companyId, Long restaurantId);

    List<RestaurantDTO> getRestaurantsForCompany(Long companyId);

    Company updateCompany(CompanyDTO companyDTO);
}
