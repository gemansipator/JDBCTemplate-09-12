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

    /**
     * Сохраняет нового пользователя в базе данных.
     * Первый зарегистрированный пользователь получает роль ADMIN, остальные — USER.
     */
    @Transactional
    public void savePerson(Person person) {
        try {
            System.out.println("Saving person: " + person.getUsername());

            // Определяем роль: первый пользователь — ADMIN, остальные — USER
            boolean isFirstUser = personRepository.count() == 0;
            person.setRole(isFirstUser ? "ROLE_ADMIN" : "ROLE_USER");

            // Шифруем пароль, если он задан
            if (person.getPassword() != null) {
                person.setPassword(passwordEncoder.encode(person.getPassword()));
            }

            // Устанавливаем метаданные создания, если они не заданы
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

    /**
     * Изменяет роль пользователя (доступно только администратору).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void changeUserRole(String username, String role) {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
        person.setRole(role);
        personRepository.save(person);
    }

    /**
     * Возвращает список всех активных пользователей (не удалённых).
     */
    public List<Person> getAllPersons() {
        return personRepository.findByRemovedAtIsNull();
    }

    /**
     * Возвращает список всех удалённых пользователей (доступно только администратору).
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Person> getAllDeletedPersons() {
        return personRepository.findByRemovedAtIsNotNull();
    }

    /**
     * Получает пользователя по ID. Если пользователь не найден, выбрасывает исключение.
     */
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    /**
     * Получает пользователя по имени пользователя (username).
     * Используется для аутентификации и назначения книг текущему пользователю.
     */
    public Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    /**
     * Удаляет пользователя (мягкое удаление, устанавливает removedAt и removedPerson).
     * Доступно только администратору.
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePerson(Long id) {
        Person person = getPersonById(id);
        person.setRemovedAt(LocalDateTime.now());
        person.setRemovedPerson("system");
        personRepository.save(person);
    }

    /**
     * Восстанавливает удалённого пользователя (очищает removedAt и removedPerson).
     * Доступно только администратору.
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void restorePerson(Long id) {
        Person person = getPersonById(id);
        person.setRemovedAt(null);
        person.setRemovedPerson(null);
        personRepository.save(person);
    }
}