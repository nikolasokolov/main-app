package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.repository.CompanyRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<Company> findById(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Override
    public void saveLogo(Company company, MultipartFile logo) throws Exception {
        try {
            company.setLogo(logo.getBytes());
        } catch (IOException e) {
            log.error("IOException caught on saveLogo company:  " + company.getName() + "message" + e.getMessage());
        }
        String fileName = logo.getOriginalFilename();
        int dotIndex = Objects.requireNonNull(fileName).lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1);
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new Exception("Invalid image format");
        }
        save(company);
    }

    @Override
    public void delete(Company company) {
        company.getRestaurants().forEach(company::removeRestaurant);
        companyRepository.delete(company);
    }

    @Override
    public List<CompanyDTO> createCompanyDTOs(List<Company> companies) {
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
}
