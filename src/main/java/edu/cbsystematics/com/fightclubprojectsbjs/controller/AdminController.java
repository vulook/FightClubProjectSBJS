package edu.cbsystematics.com.fightclubprojectsbjs.controller;

import edu.cbsystematics.com.fightclubprojectsbjs.model.AgeManager;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/fight-club")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin")
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> showOneUser(@PathVariable("id") long id) {
        Optional<User> userOptional = userService.getById(id);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<Object> createUser(@Validated @RequestBody User user, BindingResult result) {

        // Validate user details
        validateUserDetails(user, result);

        // Return a response with BAD_REQUEST status and error details
        ResponseEntity<Object> validationErrorsResponse = handleValidationErrors(result);
        if (validationErrorsResponse != null) {
            return validationErrorsResponse;
        }

        // If validation passes, create the user
        userService.createUser(user);
        logger.info("Created user {} with email: {}", user.getUsername(), user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/admin/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id,
                                             @Validated @RequestBody User updatedUser,
                                             BindingResult result) {


        logger.info("Received user ID: {} ", id);

        // Check if a user with the updated username already exists
        User usernameExisting = userService.findByUsername(updatedUser.getUsername());
        if (usernameExisting != null && !usernameExisting.getId().equals(id)) {
            result.rejectValue("username", null, "There is already an account registered with that Username");
        }

        // Check if a user with the updated email already exists
        User emailExisting = userService.findByMail(updatedUser.getEmail());
        if (emailExisting != null && !emailExisting.getId().equals(id)) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        // Return a response with BAD_REQUEST status and error details
        ResponseEntity<Object> validationErrorsResponse = handleValidationErrors(result);
        if (validationErrorsResponse != null) {
            return validationErrorsResponse;
        }

        // If validation passes, update the user
        userService.updateUser(id, updatedUser);
        logger.info("Updated user {} with email: {}", updatedUser.getUsername(), updatedUser.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        logger.info("Deleted user ID: {}", id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void validateUserDetails(User user, BindingResult result) {
        // Checking if the user already exists
        User usernameExisting = userService.findByUsername(user.getUsername());
        if (usernameExisting != null) {
            result.rejectValue("username", null, "There is already an account registered with that Username.");
        }

        // Check if the email already exists
        User emailExisting = userService.findByMail(user.getEmail());
        if (emailExisting != null) {
            result.rejectValue("email", null, "There is already an account registered with that email.");
        }

        // Check age
        boolean isPermittedAge = AgeManager.isPermittedAgeForRegistration(user.getBirthDate());
        if (isPermittedAge) {
            result.rejectValue("birthDate", null, "User age is not permitted for registration.");
        }
    }

    private ResponseEntity<Object> handleValidationErrors(BindingResult result) {
        // If there are validation errors (BAD_REQUEST)
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());

            // Create a list of error messages
            List<String> errorMessages = new ArrayList<>();

            // Add each error message to the list
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            // Return a response with BAD_REQUEST
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }

        return null;
    }

}



