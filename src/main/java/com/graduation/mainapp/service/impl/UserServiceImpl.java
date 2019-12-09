package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Authority;
import com.graduation.mainapp.model.Company;
import com.graduation.mainapp.model.User;
import com.graduation.mainapp.repository.CompanyRepository;
import com.graduation.mainapp.repository.UserRepository;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.UserAccountRequestDTO;
import com.graduation.mainapp.web.dto.UserResponseDTO;
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
    private final CompanyRepository companyRepository;

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
            Company company = null;
            if (Objects.nonNull(userAccountRequestDTO.getCompanyId())) {
                Optional<Company> optionalCompany = companyRepository.findById(userAccountRequestDTO.getCompanyId());
                if (optionalCompany.isPresent()) {
                    company = optionalCompany.get();
                }
            }
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
            throw new Exception("Could not create a new user account");
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
    @Transactional
    public List<User> findAllUsersForCompany(Long companyId) {
        return userRepository.findAllByCompanyId(companyId);
    }

    private boolean validateUserAccount(UserAccountRequestDTO userAccountRequestDTO) throws Exception {
        if (!userAccountRequestDTO.getPassword().equals(userAccountRequestDTO.getConfirmPassword())) {
            throw new Exception("Passwords doesn't match");
        } else {
            return true;
        }
    }
}
