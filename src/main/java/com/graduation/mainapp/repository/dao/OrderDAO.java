package com.graduation.mainapp.repository.dao;

import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;

import java.util.List;

public interface OrderDAO {
    List<CompanyOrdersRowMapper> getOrdersForCompany(Long companyId);
}
