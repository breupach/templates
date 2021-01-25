package ar.com.breupach.api.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ar.com.breupach.api.security.entity.Role;
import ar.com.breupach.api.security.enums.RoleName;
import ar.com.breupach.api.security.reposiroty.RoleRepository;

@SpringBootTest
public class RoleServiceTests {

	@MockBean
	private RoleRepository roleRepository;

	private RoleService roleService;
	
	@BeforeEach
	public void init() {
		roleService = new RoleService(roleRepository);
	}
	
	@Test
	public void getByRoleName() {

		Optional<Role> role = Optional.of(new Role(RoleName.ROLE_ADMIN));
		when(roleRepository.findByRoleName(RoleName.ROLE_ADMIN)).thenReturn(role);

		assertEquals(RoleName.ROLE_ADMIN, roleService.getByRoleName(RoleName.ROLE_ADMIN).get().getRoleName());
	}
	
	@Test
	public void save() {
		Role role = new Role(RoleName.ROLE_USER);
		when(roleRepository.save(role)).thenReturn(role);
		
		roleService.save(role);
		verify(roleRepository, times(1)).save(role);
	}
}
