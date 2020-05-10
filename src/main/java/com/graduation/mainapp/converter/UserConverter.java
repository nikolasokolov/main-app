package com.graduation.mainapp.converter;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.UserAccountRequestDTO;
import com.graduation.mainapp.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public static final String NOT_AVAILABLE = "N/A";

    public List<UserResponseDTO> convertToUserResponseDTOs(Collection<User> users) {
        return users.stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .authority(user.getAuthorities().stream().findFirst().get().toString())
                        .company(Objects.nonNull(user.getCompany()) ? user.getCompany().getName() : NOT_AVAILABLE)
                        .build()).collect(Collectors.toList());
    }

    public User convertToUser(UserAccountRequestDTO userAccountRequestDTO, String password, Company company) {
        return User.builder()
                .username(userAccountRequestDTO.getUsername())
                .email(userAccountRequestDTO.getEmail())
                .password(password)
                .authorities(Set.of(new Authority(userAccountRequestDTO.getAuthority())))
                .company(company)
                .build();
    }
}
