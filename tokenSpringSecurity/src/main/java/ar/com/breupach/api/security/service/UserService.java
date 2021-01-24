package ar.com.breupach.api.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.breupach.api.security.entity.User;
import ar.com.breupach.api.security.reposiroty.UserRepository;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public void save(User user) {
        this.userRepository.save(user);
    }
}
