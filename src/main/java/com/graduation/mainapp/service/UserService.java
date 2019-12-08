package com.graduation.mainapp.service;

import com.graduation.mainapp.model.User;

public interface UserService {
    User save(User user);

    void delete(User user);
}
