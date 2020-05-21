package com.graduation.mainapp.converter;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyConverter {

    public List<CompanyDTO> convertToCompanyDTOs(Collection<Company> companies) {
        return companies.stream()
                .map(this::convertToCompanyDTO)
                .collect(Collectors.toList());
    }

    public Company convertToCompany(CompanyDTO companyDTO) {
        return Company.builder()
                .name(companyDTO.getName())
                .address(companyDTO.getAddress())
                .phoneNumber(companyDTO.getPhoneNumber())
                .build();
    }

    public CompanyDTO convertToCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .phoneNumber(company.getPhoneNumber())
                .logo(company.getLogo())
                .build();
    }
}
