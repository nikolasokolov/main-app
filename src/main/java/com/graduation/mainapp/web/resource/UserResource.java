package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.User;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.UserAccount;
import com.graduation.mainapp.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserResource {
    private final UserService userService;

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserAccount userAccount) throws Exception {
        log.info("Received request for creating a new user");
        User user = userService.createUser(userAccount);
        if (Objects.nonNull(user)) {
            log.info("Successfully created a new user");
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            log.info("Could not create a new user");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
        log.info("Received request for fetching all users");
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = userService.createUserDTOs(users);
        log.info("Finished fetching all users");
        return ResponseEntity.accepted().body(userDTOs);
    }
}
