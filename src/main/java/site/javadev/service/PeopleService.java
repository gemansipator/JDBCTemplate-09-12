package site.javadev.service;

import org.springframework.transaction.annotation.Transactional;
import site.javadev.model.Person;
import site.javadev.model.PersonSecurity;
import site.javadev.repositories.PersonRepository;
import site.javadev.repositories.PersonSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PersonRepository personRepository;
    private final PersonSecurityRepository personSecurityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void savePersonSecurity(PersonSecurity personSecurity) {
        try {
            System.out.println("Saving person security: " + personSecurity.getUsername());
            personSecurity.setPassword(passwordEncoder.encode(personSecurity.getPassword()));
            personSecurity.setRole("ROLE_USER");
            System.out.println("Before saving: " + personSecurity);
            personSecurityRepository.save(personSecurity);
            System.out.println("Person security saved: " + personSecurity.getUsername());
        } catch (Exception e) {
            System.out.println("Error saving person security: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void savePerson(Person person) {
        try {
            System.out.println("Saving person: " + person.getName());
            personRepository.save(person);
            System.out.println("Person saved: " + person.getName());
        } catch (Exception e) {
            System.out.println("Error saving person: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void changeUserRole(String username, String role) {
        PersonSecurity user = personSecurityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
        user.setRole(role);
        personSecurityRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Person> getAllReaders() {
        return personRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PersonSecurity> getAllUsers() {
        return personSecurityRepository.findAll();
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
        person.setRemovedPerson("system"); // Можно заменить на текущего админа
        personRepository.save(person);
    }
}