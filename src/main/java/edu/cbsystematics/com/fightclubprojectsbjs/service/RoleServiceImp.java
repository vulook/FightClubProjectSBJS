package edu.cbsystematics.com.fightclubprojectsbjs.service;

import edu.cbsystematics.com.fightclubprojectsbjs.model.Role;
import edu.cbsystematics.com.fightclubprojectsbjs.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> getRoleById(long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> getRolesByIds(List<Long> ids) {
        return roleRepository.findByIdRoles(ids);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
