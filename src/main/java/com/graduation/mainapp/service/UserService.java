package com.graduation.mainapp.service;

import com.graduation.mainapp.model.User;
import com.graduation.mainapp.web.dto.ChangePasswordRequestDTO;
import com.graduation.mainapp.web.dto.UserAccountRequestDTO;
import com.graduation.mainapp.web.dto.UserResponseDTO;

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
}
