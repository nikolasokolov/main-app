package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.rest.dto.ChangePasswordDTO;
import com.graduation.mainapp.rest.dto.UserAccountDTO;
import com.graduation.mainapp.exception.NotFoundException;

import java.util.List;

public interface UserService {

    User save(User user);

    void deleteUser(Long userId) throws NotFoundException;

    void createUser(UserAccountDTO userAccountDTO) throws NotFoundException;

    User getUser(Long userId) throws NotFoundException;

    List<User> getAllUsers();

    List<User> getAllUsersForCompany(Long companyId);

    void changePassword(ChangePasswordDTO changePasswordDTO) throws Exception;

    List<Restaurant> getRestaurantsForUser(Long userId) throws Exception;

    User findByAuthoritiesAndCompany(Authority authority, Company company);
}
