package site.javadev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javadev.model.Person;
import site.javadev.repositories.PersonRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Person savePerson(Person person) {
        try {
            System.out.println("Saving person: " + person.getUsername());

            boolean isFirstUser = personRepository.count() == 0;
            person.setRole(isFirstUser ? "ROLE_ADMIN" : "ROLE_USER");

            if (person.getPassword() != null) {
                person.setPassword(passwordEncoder.encode(person.getPassword()));
            }

            if (person.getCreatedAt() == null) {
                person.setCreatedAt(LocalDateTime.now());
            }
            if (person.getCreatedPerson() == null) {
                person.setCreatedPerson("system");
            }

            Person savedPerson = personRepository.save(person);
            System.out.println("Person saved: " + savedPerson.getUsername());

            return savedPerson; // Теперь метод возвращает сохраненный объект
        } catch (Exception e) {
            System.out.println("Error saving person: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void changeUserRole(String username, String role) {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
        person.setRole(role);
        personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findByRemovedAtIsNull();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Person> getAllDeletedPersons() {
        return personRepository.findByRemovedAtIsNotNull();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    public Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePerson(Long id) {
        Person person = getPersonById(id);
        person.setRemovedAt(LocalDateTime.now());
        person.setRemovedPerson("system");
        personRepository.save(person);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void restorePerson(Long id) {
        Person person = getPersonById(id);
        person.setRemovedAt(null);
        person.setRemovedPerson(null);
        personRepository.save(person);
    }
}
