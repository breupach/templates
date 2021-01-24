package ar.com.breupach.api.security.controller;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.com.breupach.api.dto.Message;
import ar.com.breupach.api.security.dto.JwtDto;
import ar.com.breupach.api.security.dto.NewUser;
import ar.com.breupach.api.security.dto.UserLogin;
import ar.com.breupach.api.security.entity.Role;
import ar.com.breupach.api.security.entity.User;
import ar.com.breupach.api.security.enums.RoleName;
import ar.com.breupach.api.security.jwt.JwtProvider;
import ar.com.breupach.api.security.service.RoleService;
import ar.com.breupach.api.security.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
        	return ResponseEntity.badRequest().body(new Message("campos mal puestos o email invalido"));
        if(userService.existsByUsername(newUser.getUsername()))
        	return ResponseEntity.badRequest().body(new Message("Username existente"));
        if(userService.existsByEmail(newUser.getEmail()))
        	return ResponseEntity.badRequest().body(new Message("Email existente"));

        User user = new User(newUser.getName(), newUser.getUsername(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
        if(newUser.getRoles().contains("admin"))
            roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());

        user.setRoles(roles);

        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Message("Usuario guardado"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLogin userLogin, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
        	return ResponseEntity.badRequest().body(new Message("username o password invalido"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refreshToken(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = this.jwtProvider.refreshToken(jwtDto);

        return ResponseEntity.ok(new JwtDto(token));
    }

}
