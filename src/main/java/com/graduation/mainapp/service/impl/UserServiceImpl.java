package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.User;
import com.graduation.mainapp.repository.UserRepository;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        user.setAuthorities(null);
        userRepository.delete(user);
    }
}
