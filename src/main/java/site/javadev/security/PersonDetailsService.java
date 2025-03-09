package site.javadev.security;

import site.javadev.model.PersonSecurity;
import site.javadev.repositories.PeopleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PersonSecurity> person = peopleRepository.findByUsername(username);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new PersonDetails(person.get());
    }
}