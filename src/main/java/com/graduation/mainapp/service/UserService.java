package com.graduation.mainapp.service;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.ChangePasswordRequestDTO;
import com.graduation.mainapp.dto.UserAccountRequestDTO;
import com.graduation.mainapp.dto.UserResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    void delete(User user);

    User createUser(UserAccountRequestDTO userAccountRequestDTO) throws Exception;

    List<User> findAll();

    List<UserResponseDTO> createUserDTOs(Collection<User> users);

    Optional<User> findById(Long userId);

    List<User> findAllUsersForCompany(Long companyId);

    boolean changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception;

    List<Restaurant> getRestaurantsForUser(Long userId) throws Exception;

    User findByIdOrThrow(Long userId) throws DomainObjectNotFoundException;

    User findByAuthoritiesAndCompany(Authority authority, Company company);
}
