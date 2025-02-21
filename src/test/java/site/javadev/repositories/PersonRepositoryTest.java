package site.javadev.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.javadev.model.Person;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Person Repository Test")
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSavePerson() {
        Person person = new Person();
        person.setFullName("John Doe");
        person.setBirthYear(1990);

        Person savedPerson = personRepository.save(person);

        assertNotNull(savedPerson.getId());
        assertEquals("John Doe", savedPerson.getFullName());
    }

    @Test
    public void testFindPersonById() {
        Person person = new Person();
        person.setFullName("Jane Doe");
        person.setBirthYear(1988);
        personRepository.save(person);

        Optional<Person> foundPerson = personRepository.findById(person.getId());

        assertTrue(foundPerson.isPresent());
        assertEquals("Jane Doe", foundPerson.get().getFullName());
    }

    @Test
    public void testFindAllPersons() {
        Person person1 = new Person();
        person1.setFullName("Person 1");
        person1.setBirthYear(1995);
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setFullName("Person 2");
        person2.setBirthYear(1996);
        personRepository.save(person2);

        List<Person> persons = personRepository.findAll();

        assertEquals(2, persons.size());
    }
}
