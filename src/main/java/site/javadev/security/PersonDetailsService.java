package site.javadev.security;

import site.javadev.model.PersonSecurity;
import site.javadev.repositories.PersonSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PersonSecurityRepository personSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PersonSecurity personSecurity = personSecurityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.builder()
                .username(personSecurity.getUsername())
                .password(personSecurity.getPassword())
                .roles(personSecurity.getRole().replace("ROLE_", ""))
                .build();
    }
}