package site.javadev.service;

import site.javadev.dao.UserRepository;
import site.javadev.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String password, String role) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
