package com.graduation.mainapp.repository;

import com.graduation.mainapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByCompanyId(Long companyId);
    Optional<User> findOneByUsername(String username);
}
