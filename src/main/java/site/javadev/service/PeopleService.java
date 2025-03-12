package site.javadev.service;

import site.javadev.model.Person;
import site.javadev.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public void savePerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changeUserRole(String name, String role) {
        Person user = personRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + name + " не найден"));
        user.setRole(role);
        personRepository.save(user);
    }

    public List<Person> getAllUsers() {
        return personRepository.findAll();
    }
}