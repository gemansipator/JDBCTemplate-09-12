package site.javadev.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.model.PersonSecurity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Использует H2 по умолчанию
public class RepositoriesTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private PersonRepository personRepository;

    // Тесты для BookRepository
    @Test
    public void testSaveBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2023);

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    public void testFindBookById() {
        Book book = new Book();
        book.setTitle("Find Me");
        book.setAuthor("Author");
        book.setPublicationYear(2020);
        bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals("Find Me", foundBook.get().getTitle());
    }

    @Test
    public void testFindAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setPublicationYear(2021);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setPublicationYear(2022);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();
        assertEquals(2, books.size());
    }

    @Test
    public void testDeleteBookById() {
        Book book = new Book();
        book.setTitle("Delete Me");
        book.setAuthor("Author");
        book.setPublicationYear(2019);
        bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        Optional<Book> deletedBook = bookRepository.findById(book.getId());
        assertFalse(deletedBook.isPresent());
    }

    // Тесты для PeopleRepository
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

    // Тесты для PersonRepository
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
        person1.setBirthYear(1991);
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setFullName("Person 2");
        person2.setBirthYear(1992);
        personRepository.save(person2);

        List<Person> persons = personRepository.findAll();
        assertEquals(2, persons.size());
    }

    @Test
    public void testDeletePersonById() {
        Person person = new Person();
        person.setFullName("Delete Me");
        person.setBirthYear(1987);
        personRepository.save(person);

        personRepository.deleteById(person.getId());

        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertFalse(deletedPerson.isPresent());
    }
}