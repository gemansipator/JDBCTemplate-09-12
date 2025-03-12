package site.javadev.service;

import site.javadev.model.Person;
import site.javadev.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void savePerson(Person person) {
        try {
            System.out.println("Saving person: " + person.getUsername());
            if (person.getPassword() != null) {
                person.setPassword(passwordEncoder.encode(person.getPassword()));
            }
            if (person.getRole() == null) {
                person.setRole("ROLE_USER");
            }
            if (person.getCreatedAt() == null) {
                person.setCreatedAt(LocalDateTime.now());
            }
            if (person.getCreatedPerson() == null) {
                person.setCreatedPerson("system");
            }
            personRepository.save(person);
            System.out.println("Person saved: " + person.getUsername());
        } catch (Exception e) {
            System.out.println("Error saving person: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changeUserRole(String username, String role) {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
        person.setRole(role);
        personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePerson(Long id) {
        Person person = getPersonById(id);
        person.setRemovedAt(LocalDateTime.now());
        person.setRemovedPerson("system");
        personRepository.save(person);
    }
}