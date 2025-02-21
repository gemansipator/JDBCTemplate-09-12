package site.javadev.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.javadev.model.PersonSecurity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PeopleRepositoryTest {

    @Autowired
    private PeopleRepository peopleRepository;

    @Test
    public void testSavePersonSecurity() {
        PersonSecurity person = new PersonSecurity();
        person.setUsername("testuser");
        person.setPassword("password");
        person.setYearOfBirth(1990);
        person.setRole("ROLE_USER");

        PersonSecurity savedPerson = peopleRepository.save(person);

        assertNotNull(savedPerson.getId());
        assertEquals("testuser", savedPerson.getUsername());
    }

    @Test
    public void testFindPersonSecurityById() {
        PersonSecurity person = new PersonSecurity();
        person.setUsername("findme");
        person.setPassword("pass");
        person.setYearOfBirth(1985);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        Optional<PersonSecurity> foundPerson = peopleRepository.findById(person.getId());

        assertTrue(foundPerson.isPresent());
        assertEquals("findme", foundPerson.get().getUsername());
    }

    @Test
    public void testFindAllPersonSecurity() {
        PersonSecurity person1 = new PersonSecurity();
        person1.setUsername("user1");
        person1.setPassword("pass1");
        person1.setYearOfBirth(1995);
        person1.setRole("ROLE_USER");
        peopleRepository.save(person1);

        PersonSecurity person2 = new PersonSecurity();
        person2.setUsername("user2");
        person2.setPassword("pass2");
        person2.setYearOfBirth(1996);
        person2.setRole("ROLE_USER");
        peopleRepository.save(person2);

        List<PersonSecurity> people = peopleRepository.findAll();

        assertEquals(2, people.size());
    }

    @Test
    public void testDeletePersonSecurityById() {
        PersonSecurity person = new PersonSecurity();
        person.setUsername("deleteuser");
        person.setPassword("pass");
        person.setYearOfBirth(1980);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        peopleRepository.deleteById(person.getId());

        Optional<PersonSecurity> deletedPerson = peopleRepository.findById(person.getId());
        assertFalse(deletedPerson.isPresent());
    }

    @Test
    public void testFindByUsername() {
        PersonSecurity person = new PersonSecurity();
        person.setUsername("uniqueuser");
        person.setPassword("pass");
        person.setYearOfBirth(1990);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        Optional<PersonSecurity> foundPerson = peopleRepository.findByUsername("uniqueuser");

        assertTrue(foundPerson.isPresent());
        assertEquals("uniqueuser", foundPerson.get().getUsername());

        Optional<PersonSecurity> notFoundPerson = peopleRepository.findByUsername("nonexistent");
        assertFalse(notFoundPerson.isPresent());
    }
}
