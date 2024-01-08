package edu.cbsystematics.com.fightclubprojectsbjs;

import edu.cbsystematics.com.fightclubprojectsbjs.model.Role;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import edu.cbsystematics.com.fightclubprojectsbjs.service.RoleService;
import edu.cbsystematics.com.fightclubprojectsbjs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

<<<<<<< HEAD
/**
 * FileName: FightClubProjectSBJSApplication
 * Author:   Andriy V
 * Date:     26.12.2023 17:20
 * Description:
 */

=======
>>>>>>> 1ce38dd (Initial commit)
@SpringBootApplication
public class FightClubProjectSBJSApplication {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public FightClubProjectSBJSApplication(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    public static void main(String[] args) {
        SpringApplication.run(FightClubProjectSBJSApplication.class, args);
    }


    @PostConstruct
    public void createAndSaveRolesAndUsers() {

        // Creating and Saving the roles in the database
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");

        roleService.createRole(adminRole);
        roleService.createRole(userRole);

        // Creating and Saving users with roles
        User admin = new User("promoter", LocalDate.of(1988, 1, 15), "admin@example.com", "123", true);
        User user = new User("fighter", LocalDate.now().minusYears(28), "fighter@example.com", "123", true);

        userService.createUser(admin);
        userService.createUser(user);

        userService.addRoleToUser(admin, adminRole.getId());
        userService.addRoleToUser(admin, userRole.getId());
        userService.addRoleToUser(user, userRole.getId());

    }

}

