package com.graduation.mainapp.service.impl;

import com.google.common.collect.Lists;
import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.ChangePasswordRequestDTO;
import com.graduation.mainapp.dto.UserAccountRequestDTO;
import com.graduation.mainapp.dto.UserResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.exception.InvalidCredentialsException;
import com.graduation.mainapp.repository.UserRepository;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        user.setAuthorities(null);
        userRepository.delete(user);
    }

    @Override
    public User createUser(UserAccountRequestDTO userAccountRequestDTO) throws Exception {
        boolean userAccountIsValid = validateUserAccount(userAccountRequestDTO);
        if (userAccountIsValid) {
            Company company = companyService.findByIdOrThrow(userAccountRequestDTO.getCompanyId());
            Set<Authority> authorities = new HashSet<>();
            authorities.add(new Authority(userAccountRequestDTO.getAuthority()));
            User user = User.builder()
                    .username(userAccountRequestDTO.getUsername())
                    .email(userAccountRequestDTO.getEmail())
                    .password(passwordEncoder.encode(userAccountRequestDTO.getPassword()))
                    .authorities(authorities)
                    .company(company)
                    .build();
            return save(user);
        } else {
            throw new InvalidCredentialsException("Could not create a new user account");
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserResponseDTO> createUserDTOs(Collection<User> users) {
        List<UserResponseDTO> userResponseDTOS = new LinkedList<>();
        users.forEach(user -> {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setUsername(user.getUsername());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setAuthority(user.getAuthorities().stream().findFirst().get().toString());
            userResponseDTO.setCompany(Objects.nonNull(user.getCompany()) ? user.getCompany().getName() : "N/A");
            userResponseDTOS.add(userResponseDTO);
        });
        return userResponseDTOS;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User findByIdOrThrow(Long userId) throws DomainObjectNotFoundException {
        return userRepository.findById(userId).
                orElseThrow(() -> new DomainObjectNotFoundException("User with ID " + userId + " is not found"));
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

    private boolean validateUserAccount(UserAccountRequestDTO userAccountRequestDTO) throws Exception {
        if (!userAccountRequestDTO.getPassword().equals(userAccountRequestDTO.getConfirmPassword())) {
            throw new InvalidCredentialsException("Passwords doesn't match");
        } else {
            return true;
        }
    }

    @Override
    @Transactional
    public boolean changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception {
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
                this.save(user);
                return true;
            } else {
                throw new InvalidCredentialsException("Current password is not correct");
            }
        }
        return false;
    }

    @Override
    @Transactional
    public List<Restaurant> getRestaurantsForUser(Long userId) throws Exception {
        User user = userRepository.getOne(userId);
        Company company = companyService.findByIdOrThrow(user.getCompany().getId());
        return Lists.newArrayList(company.getRestaurants());
    }
}
