package edu.cbsystematics.com.fightclubprojectsbjs.service;

import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Saves a user to the database.
    void createUser(User user);

    // Register a user to the database.
    @Transactional
    User registerNewUser(User user);

    // Add a role to a user based on the role ID.
    @Transactional
    void addRoleToUser(User user, Long roleId);

    // Updates user information by user ID.
    void updateUser(Long userId, User updatedUser);

    // Deletes a user by user ID.
    void deleteUser(Long id);

    // Retrieves a list of all users.
    List<User> getAllUsers();

    // Retrieves a user by user ID.
    Optional<User> getById(Long id);

    // Retrieves a user by nick.
    User findByUsername(String username);

    User findByMail(String email);
}




