package edu.cbsystematics.com.fightclubprojectsbjs.controller;

import edu.cbsystematics.com.fightclubprojectsbjs.internal.PortAggregator;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/server")
public class UsersRestServerController {

    private static final Logger logger = LoggerFactory.getLogger(UsersRestServerController.class);

    private final UserService userService;

    private final PortAggregator portAggregator;

    @Autowired
    public UsersRestServerController(UserService userService, PortAggregator portAggregator) {
        this.userService = userService;
        this.portAggregator = portAggregator;
    }

    // build port REST API
    // http://localhost:8080/server/port
    @GetMapping("/port")
    public ResponseEntity<Integer> getServerPort() {
        int serverPort = portAggregator.getServerPort();
        logger.info("Server Port: {}", serverPort);
        return ResponseEntity.ok().body(serverPort);
    }

    // Build Get All Users REST API
    // http://localhost:8080/server/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error occurred while fetching users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
