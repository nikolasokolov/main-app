package com.graduation.mainapp.web.resource;

import com.graduation.mainapp.model.User;
import com.graduation.mainapp.service.CompanyService;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.UserAccount;
import com.graduation.mainapp.web.dto.UserDTO;
import com.netflix.ribbon.proxy.annotation.Http;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        log.info("Received request for deleting user with ID [{}]", userId);
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            userService.delete(user.get());
            log.info("Successfully deleted user with ID [{}]", userId);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            log.warn("User with ID [{}] is not found", userId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/company/{companyId}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsersForCompany(@PathVariable Long companyId) {
        log.info("Received request for fetching all users");
        List<User> users = userService.findAllUsersForCompany(companyId);
        List<UserDTO> userDTOs = userService.createUserDTOs(users);
        log.info("Finished fetching all users");
        return ResponseEntity.accepted().body(userDTOs);
    }
}
