package ar.com.breupach.api.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.breupach.api.security.entity.Role;
import ar.com.breupach.api.security.enums.RoleName;
import ar.com.breupach.api.security.reposiroty.RoleRepository;

@Service
@Transactional
public class RoleService {

	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Optional<Role> getByRoleName(RoleName roleName) {
		return this.roleRepository.findByRoleName(roleName);
	}

	public void save(Role role) {
		this.roleRepository.save(role);
	}
}
