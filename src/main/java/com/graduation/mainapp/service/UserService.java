package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.ChangePasswordRequestDTO;
import com.graduation.mainapp.dto.UserAccountRequestDTO;
import com.graduation.mainapp.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    void deleteUser(Long userId) throws NotFoundException;

    void createUser(UserAccountRequestDTO userAccountRequestDTO) throws NotFoundException;

    User getUser(Long userId) throws NotFoundException;

    List<User> getAllUsers();

    List<User> findAllUsersForCompany(Long companyId);

    void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception;

    List<Restaurant> getRestaurantsForUser(Long userId) throws Exception;

    User findByAuthoritiesAndCompany(Authority authority, Company company);
}
