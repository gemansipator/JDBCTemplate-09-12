package site.javadev.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.javadev.model.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Person Repository Test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    public void setUp() {
        // Создаём и сохраняем пользователя
        person = new Person();
        person.setName("John Doe");
        person.setAge(30);
        person.setEmail("john@example.com");
        person.setPhoneNumber("+123456789");
        person.setPassword("password");
        person.setRole("ROLE_USER");
        person.setUsername("johndoe");
        person.setCreatedAt(LocalDateTime.now());
        person.setCreatedPerson("admin");
        personRepository.save(person);
    }

    @Test
    @DisplayName("Проверка сохранения пользователя")
    public void testSavePerson() {
        assertNotNull(person.getId());
        assertEquals("John Doe", person.getName());
        assertEquals("johndoe", person.getUsername());
        assertEquals("ROLE_USER", person.getRole());
    }

    @Test
    @DisplayName("Проверка поиска пользователя по ID")
    public void testFindPersonById() {
        Optional<Person> foundPerson = personRepository.findById(person.getId());
        assertTrue(foundPerson.isPresent());
        assertEquals("John Doe", foundPerson.get().getName());
        assertEquals("johndoe", foundPerson.get().getUsername());
    }

    @Test
    @DisplayName("Проверка поиска всех пользователей")
    public void testFindAllPersons() {
        Person person2 = new Person();
        person2.setName("Jane Doe");
        person2.setAge(25);
        person2.setEmail("jane@example.com");
        person2.setPhoneNumber("+987654321");
        person2.setPassword("pass");
        person2.setRole("ROLE_ADMIN");
        person2.setUsername("janedoe");
        person2.setCreatedAt(LocalDateTime.now());
        person2.setCreatedPerson("admin"); // Указано значение
        personRepository.save(person2);

        List<Person> persons = personRepository.findAll();
        assertEquals(2, persons.size());
    }

    @Test
    @DisplayName("Проверка удаления пользователя")
    public void testDeletePerson() {
        personRepository.delete(person);
        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertFalse(deletedPerson.isPresent());
    }

    @Test
    @DisplayName("Проверка поиска пользователя по имени пользователя")
    public void testFindByUsername() {
        Optional<Person> foundPerson = personRepository.findByUsername("johndoe");
        assertTrue(foundPerson.isPresent());
        assertEquals("johndoe", foundPerson.get().getUsername());
        assertEquals("John Doe", foundPerson.get().getName());

        Optional<Person> notFoundPerson = personRepository.findByUsername("nonexistent");
        assertFalse(notFoundPerson.isPresent());
    }

    @Test
    @DisplayName("Проверка мягкого удаления пользователя")
    public void testSoftDeletePerson() {
        person.setRemovedAt(LocalDateTime.now());
        person.setRemovedPerson("admin");
        personRepository.save(person);

        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertTrue(deletedPerson.isPresent());
        assertNotNull(deletedPerson.get().getRemovedAt());
        assertEquals("admin", deletedPerson.get().getRemovedPerson());
    }
}