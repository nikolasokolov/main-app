package com.graduation.mainapp.converter;

import com.graduation.mainapp.domain.Authority;
import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.rest.dto.RestaurantAccountDTO;
import com.graduation.mainapp.rest.dto.UserAccountDTO;
import com.graduation.mainapp.rest.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public static final String NOT_AVAILABLE = "N/A";
    public static final String ROLE_RESTAURANT = "ROLE_RESTAURANT";

    public List<UserDTO> convertToUserResponseDTOs(Collection<User> users) {
        return users.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .authority(user.getAuthorities().stream().findFirst().get().toString())
                        .company(Objects.nonNull(user.getCompany()) ? user.getCompany().getName() : NOT_AVAILABLE)
                        .build())
                .collect(Collectors.toList());
    }

    public User convertToUser(UserAccountDTO userAccountDTO, String password, Company company) {
        return User.builder()
                .username(userAccountDTO.getUsername())
                .email(userAccountDTO.getEmail())
                .password(password)
                .authorities(Set.of(new Authority(userAccountDTO.getAuthority())))
                .company(company)
                .build();
    }

    public User convertToUser(RestaurantAccountDTO restaurantAccountDTO, String password) {
        return User.builder()
                .username(restaurantAccountDTO.getUsername())
                .email(restaurantAccountDTO.getEmail())
                .password(password)
                .authorities(Set.of(new Authority(ROLE_RESTAURANT)))
                .build();
    }
}
