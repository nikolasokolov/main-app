package com.graduation.mainapp.service;

import com.graduation.mainapp.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> findAll();

    Company save(Company company);

    Optional<Company> findById(Long companyId);
}
