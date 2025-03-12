package site.javadev.service;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void savePerson(Person person) {
        System.out.println("Saving person: " + person.getName());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER"); // Устанавливаем роль, если она не задана в форме
        System.out.println("Before saving: " + person);
        personRepository.save(person);
        System.out.println("Person saved: " + person.getName());
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