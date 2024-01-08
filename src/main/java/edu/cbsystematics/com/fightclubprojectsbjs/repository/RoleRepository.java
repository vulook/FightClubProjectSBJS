package edu.cbsystematics.com.fightclubprojectsbjs.repository;

import edu.cbsystematics.com.fightclubprojectsbjs.model.Role;
import edu.cbsystematics.com.fightclubprojectsbjs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // The query selects Role entities where the ID is in the provided list of IDs.
    @Query("SELECT a FROM Role a WHERE a.id IN :ids")
    List<Role> findByIdRoles(@Param("ids") List<Long> ids);

    // Find a role by its name in the Role entity.
    Optional<Role> findByRoleName(String roleName);

}
