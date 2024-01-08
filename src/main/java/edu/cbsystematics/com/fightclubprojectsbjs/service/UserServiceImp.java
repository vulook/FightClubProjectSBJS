package edu.cbsystematics.com.fightclubprojectsbjs.service;

<<<<<<< HEAD
import edu.cbsystematics.com.fightclubprojectsbjs.exception.ResourceNotFoundException;
=======
>>>>>>> 1ce38dd (Initial commit)
import edu.cbsystematics.com.fightclubprojectsbjs.model.Role;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.repository.RoleRepository;
import edu.cbsystematics.com.fightclubprojectsbjs.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImp implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository =  userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(User user) {
        // Create a verification code
        String verificationCode = UUID.randomUUID().toString();
        logger.info("Generated verification code: {}", verificationCode);
        user.setVerificationCode(verificationCode);

        // Set the registration date
        user.setDataReg(LocalDateTime.now());

        // Encrypt the user's password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user in the database
        userRepository.save(user);
        logger.info("User saved. ID: {}, Username: {}", user.getId(), user.getUsername());

    }

    @Override
    @Transactional
    public User registerNewUser(User user) {
        String verificationCode = UUID.randomUUID().toString();
        logger.info("Generated verification code: {}", verificationCode);
        user.setVerificationCode(verificationCode);

        // Current date and time of registration
        user.setDataReg(LocalDateTime.now());

        // Disable the account until it is verified
        user.setEnabled(false);

        // Encrypt the user's password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Initialize role collection
        user.setRoles(new HashSet<>());

        // Find or create ROLE_FIGHTER and add it to the user's roles
        Role fighterRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));

        user.getRoles().add(fighterRole);

        // Save the user in the database
        userRepository.save(user);
        logger.info("User saved. ID: {}, Username: {}, Role: {}   ", user.getId(), user.getUsername(), user.getRoles().stream().map(Role::getRoleName).collect(Collectors.joining(", ")));

        return user;
    }

    @Override
    @Transactional
    public void addRoleToUser(User user, Long roleId) {
        try {
            // Retrieve the role to add
            Role roleToAdd = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            List<Role> existingRoles = new ArrayList<>(user.getRoles());

            // Check if the user already has the role
            if (!existingRoles.contains(roleToAdd)) {
                // Add the role to the user's roles
                existingRoles.add(roleToAdd);
                user.setRoles(existingRoles);
                userRepository.save(user);

                logger.info("Role {} added to user {}", roleToAdd.getRoleName(), user.getUsername());
            } else {
                logger.info("User {} already has role {}", user.getUsername(), roleToAdd.getRoleName());
            }

<<<<<<< HEAD
        } catch (ResourceNotFoundException ex) {
            logger.error("Error adding role to user: {}", user.getUsername(), ex);
=======
        } catch (Exception ex) {
            logger.error("Error adding role to user: {}", user.getUsername(), ex);
            throw new RuntimeException("Failed to add role", ex);
>>>>>>> 1ce38dd (Initial commit)
        }
    }

    @Override
    public void updateUser(Long userId, User updatedUser) {
        try {
            // Retrieve the user to update
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            // Check if the username has changed
            if (!existingUser.getUsername().equals(updatedUser.getUsername())) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            // Check if the birthDate has changed
            if (!existingUser.getBirthDate().equals(updatedUser.getBirthDate())) {
                existingUser.setBirthDate(existingUser.getBirthDate());
            }

             // Check if the email has changed
            if (!existingUser.getEmail().trim().equals(updatedUser.getEmail().trim())) {
                existingUser.setEmail(existingUser.getEmail());
            }

            // Encrypt the password if it has changed
            if (!passwordEncoder.matches(existingUser.getPassword(), updatedUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // Check if roles have changed
            if (!existingUser.getRoles().equals(updatedUser.getRoles())) {
                existingUser.setRoles(updatedUser.getRoles());
            }

            userRepository.save(existingUser);
            logger.info("User data updated for user: {}", existingUser.getUsername());

<<<<<<< HEAD
        } catch (ResourceNotFoundException ex) {
            logger.error("Error updating user data: {}", userId, ex);
        }

=======
        } catch (Exception ex) {
            logger.error("Error updating user data: {}", userId, ex);
            throw new RuntimeException("Failed to update user data", ex);
        }
>>>>>>> 1ce38dd (Initial commit)
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByMail(String email) {
        return userRepository.findByEmail(email);
    }

}
