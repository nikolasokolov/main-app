package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.rest.dto.CompanyDTO;
import com.graduation.mainapp.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {

    List<Company> getAllCompanies();

    Company save(Company company);

    Company getCompany(Long companyId) throws NotFoundException;

    Company saveLogo(Long companyId, MultipartFile logo) throws Exception;

    boolean delete(Long companyId) throws NotFoundException;

    Company updateCompany(CompanyDTO companyDTO) throws NotFoundException;
}
