package com.graduation.mainapp.service;

import com.graduation.mainapp.model.User;
import com.graduation.mainapp.web.dto.UserAccount;
import com.graduation.mainapp.web.dto.UserDTO;

import java.util.Collection;
import java.util.List;

public interface UserService {
    User save(User user);

    void delete(User user);

    User createUser(UserAccount userAccount) throws Exception;

    List<User> findAll();

    List<UserDTO> createUserDTOs(Collection<User> users);
}
