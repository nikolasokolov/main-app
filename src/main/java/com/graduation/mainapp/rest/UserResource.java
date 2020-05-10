package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.UserConverter;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.dto.ChangePasswordRequestDTO;
import com.graduation.mainapp.dto.UserAccountRequestDTO;
import com.graduation.mainapp.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final UserConverter userConverter;

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@RequestBody UserAccountRequestDTO userAccountRequestDTO) throws Exception {
        log.info("Started creating a new user with username=[{}]", userAccountRequestDTO.getUsername());
        userService.createUser(userAccountRequestDTO);
        log.info("Finished creating a new user with username=[{}]", userAccountRequestDTO.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Started fetching all users");
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOs = userConverter.convertToUserResponseDTOs(users);
        log.info("Finished fetching all users");
        return ResponseEntity.ok().body(userResponseDTOs);
    }

    @RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) throws NotFoundException {
        log.info("Started deleting user with ID=[{}]", userId);
        userService.deleteUser(userId);
        log.info("Finished deleting user with ID=[{}]", userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/company/{companyId}/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getAllUsersForCompany(@PathVariable Long companyId) {
        log.info("Started fetching all users for company with ID=[{}]", companyId);
        List<User> users = userService.findAllUsersForCompany(companyId);
        List<UserResponseDTO> userResponseDTOs = userConverter.convertToUserResponseDTOs(users);
        log.info("Finished fetching all users for company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(userResponseDTOs);
    }

    @RequestMapping(value = "/users/change-password", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception {
        userService.changePassword(changePasswordRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
