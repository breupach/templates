package ar.com.breupach.api.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import ar.com.breupach.api.security.entity.User;

@SpringBootTest
public class UserDetailsServiceImplTests {

	@MockBean
	UserService userService;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Test
	public void loadUserByUsername() {
		
		String username = "prueba";
		
		Optional<User> userMock = Optional.of(new User("prueba", "prueba", "prueba", "prueba"));	
		when(userService.getByUsername(username)).thenReturn(userMock);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
		
		assertEquals("prueba", userDetails.getUsername());
	}
}
