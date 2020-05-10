package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.converter.UserConverter;
import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.ChangePasswordRequestDTO;
import com.graduation.mainapp.dto.UserAccountRequestDTO;
import com.graduation.mainapp.exception.InvalidCredentialsException;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.repository.OrderRepository;
import com.graduation.mainapp.repository.UserRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final OrderRepository orderRepository;
    private final UserConverter userConverter;

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void createUser(UserAccountRequestDTO userAccountRequestDTO) throws NotFoundException {
        if (userAccountRequestDTO.getPassword().equals(userAccountRequestDTO.getConfirmPassword())) {
            Company company = companyService.findByIdOrThrow(userAccountRequestDTO.getCompanyId());
            String password = passwordEncoder.encode(userAccountRequestDTO.getPassword());
            User user = userConverter.convertToUser(userAccountRequestDTO, password, company);
            save(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with ID=[" + userId + "] is not found"));
    }

    @Override
    public User findByAuthoritiesAndCompany(Authority authority, Company company) {
        return userRepository.findByAuthoritiesAndCompany(authority, company);
    }

    @Override
    @Transactional
    public List<User> findAllUsersForCompany(Long companyId) {
        return userRepository.findAllByCompanyId(companyId);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception {
        if (!changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmPassword())) {
            throw new InvalidCredentialsException("Passwords doesn't match");
        }
        Optional<User> userOptional = userRepository.findOneByUsername(changePasswordRequestDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String currentPasswordFromRequest = changePasswordRequestDTO.getCurrentPassword();
            String newPasswordEncrypted = passwordEncoder.encode(changePasswordRequestDTO.getNewPassword());
            boolean currentPasswordMatch = passwordEncoder.matches(currentPasswordFromRequest, user.getPassword());
            if (currentPasswordMatch) {
                user.setPassword(newPasswordEncrypted);
                save(user);
            } else {
                throw new InvalidCredentialsException("Current password is not correct");
            }
        }
    }

    @Override
    @Transactional
    public List<Restaurant> getRestaurantsForUser(Long userId) throws Exception {
        User user = userRepository.getOne(userId);
        Company company = companyService.findByIdOrThrow(user.getCompany().getId());
        return new LinkedList<>(company.getRestaurants());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) throws NotFoundException {
        User user = getUser(userId);
        orderRepository.deleteAllByUser(user);
        user.setAuthorities(null);
        userRepository.delete(user);
    }
}
