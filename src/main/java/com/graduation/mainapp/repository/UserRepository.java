package com.graduation.mainapp.repository;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByCompanyId(Long companyId);

    Optional<User> findOneByUsername(String username);

    User findByAuthoritiesAndCompany(Authority authority, Company company);
}
