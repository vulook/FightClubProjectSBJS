package edu.cbsystematics.com.fightclubprojectsbjs.controller;

import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.service.UserService;
import edu.cbsystematics.com.fightclubprojectsbjs.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/fight-club")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retrieves user information based on the currently authenticated user.
    @GetMapping("/user")
    public ResponseEntity<User> userGet(Principal principal) {
        return new ResponseEntity<>(userService.findByUsername(principal.getName()), HttpStatus.OK);
    }

}
