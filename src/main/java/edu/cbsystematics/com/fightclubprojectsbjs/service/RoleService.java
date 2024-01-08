package edu.cbsystematics.com.fightclubprojectsbjs.service;

import edu.cbsystematics.com.fightclubprojectsbjs.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    // Creates a new role.
    void createRole(Role role);

    // Deletes a role by its ID.
    void deleteRole(long id);

    // Retrieves a role by its ID.
    Optional<Role> getRoleById(long id);

    // Retrieves roles by a list of role IDs.
    List<Role> getRolesByIds(List<Long> ids);

    // Retrieves all roles.
    List<Role> getAllRoles();
}

