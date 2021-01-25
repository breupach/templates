package ar.com.breupach.api.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ar.com.breupach.api.security.entity.User;
import ar.com.breupach.api.security.reposiroty.UserRepository;

@SpringBootTest
public class UserServiceTests {

	@MockBean
	UserRepository userRepository;

	@Autowired
	UserService userService;
	
	@BeforeEach
	public void init() {
		userService = new UserService(userRepository);
	}

	@DisplayName("getByUsername -> equals")
	@Test
	public void getByUsername() {
		String username = "prueba";

		Optional<User> userMok = Optional.of(new User("prueba", "prueba", "prueba", "prueba"));

		when(userRepository.findByUsername(username)).thenReturn(userMok);

		User userAssert = userService.getByUsername(username).get();

		assertEquals(userAssert.getUsername(), username);
	}
	
	@DisplayName("existsByUsername -> true")
	@Test
	public void existsByUsernameTrue() {
		String username = "prueba";
		when(userRepository.existsByUsername(username)).thenReturn(true);
		
		assertTrue(userService.existsByUsername(username));
	}
	
	@DisplayName("existsByUsername -> false")
	@Test
	public void existsByUsernameFalse() {
		String username = "prueba";
		when(userRepository.existsByUsername(username)).thenReturn(false);
		
		assertFalse(userService.existsByUsername(username));
	}
	
	@DisplayName("existsByEmail -> true")
	@Test
	public void existsByEmailTrue() {
		String email = "prueba";
		when(userRepository.existsByEmail(email)).thenReturn(true);
		
		assertTrue(userService.existsByEmail(email));
	}
	
	@DisplayName("existsByEmail -> false")
	@Test
	public void existsByEmailFalse() {
		String email = "prueba";
		when(userRepository.existsByEmail(email)).thenReturn(false);
		
		assertFalse(userService.existsByEmail(email));
	}
	
	@DisplayName("save -> que se llame una vez")
	@Test
	public void save() {
		User user = new User("prueba", "prueba", "prueba", "prueba");
		
		when(userRepository.save(user)).thenReturn(user);
		userService.save(user);
		verify(userRepository, times(1)).save(user);
	}
}
