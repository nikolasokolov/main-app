package com.graduation.mainapp.rest;

import com.graduation.mainapp.converter.UserConverter;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.exception.NotFoundException;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.rest.dto.ChangePasswordDTO;
import com.graduation.mainapp.rest.dto.UserAccountDTO;
import com.graduation.mainapp.rest.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/main/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final UserConverter userConverter;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@RequestBody @Valid UserAccountDTO userAccountDTO) throws Exception {
        log.info("Started creating a new User with username=[{}]", userAccountDTO.getUsername());
        userService.createUser(userAccountDTO);
        log.info("Finished creating a new User with username=[{}]", userAccountDTO.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Started fetching all Users");
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOS = userConverter.convertToUserResponseDTOs(users);
        log.info("Finished fetching all Users");
        return ResponseEntity.ok().body(userDTOS);
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) throws NotFoundException {
        log.info("Started deleting User with ID=[{}]", userId);
        userService.deleteUser(userId);
        log.info("Finished deleting User with ID=[{}]", userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/company/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAllUsersForCompany(@PathVariable Long companyId) {
        log.info("Started fetching all users for Company with ID=[{}]", companyId);
        List<User> users = userService.findAllUsersForCompany(companyId);
        List<UserDTO> userDTOS = userConverter.convertToUserResponseDTOs(users);
        log.info("Finished fetching all users for Company with ID=[{}]", companyId);
        return ResponseEntity.ok().body(userDTOS);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) throws Exception {
        log.info("Started changing password on User with username=[{}]", changePasswordDTO.getUsername());
        userService.changePassword(changePasswordDTO);
        log.info("Finished changing password on User with username=[{}]", changePasswordDTO.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
