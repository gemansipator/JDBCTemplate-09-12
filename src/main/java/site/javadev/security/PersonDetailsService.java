package site.javadev.security;

import site.javadev.model.Person;
import site.javadev.repositories.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByName(name);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found with name: " + name);
        }
        return new PersonDetails(person.get());
    }
}