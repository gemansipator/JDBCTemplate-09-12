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

/**
 * Класс для тестирования репозиториев BookRepository, PeopleRepository и PersonRepository.
 * Аннотация @DataJpaTest указывает, что это тесты для слоя доступа к данным (JPA),
 * и Spring Boot автоматически настроит тестовую среду с использованием in-memory базы H2.
 */
@DataJpaTest
public class RepositoriesTest {

    // Внедряем репозитории через @Autowired, чтобы Spring предоставил их экземпляры для тестов.
    @Autowired
    private BookRepository bookRepository; // Репозиторий для работы с книгами

    @Autowired
    private PeopleRepository peopleRepository; // Репозиторий для работы с пользователями (PersonSecurity)

    @Autowired
    private PersonRepository personRepository; // Репозиторий для работы с людьми (Person)

    // Тесты для BookRepository
    /**
     * Тест проверяет метод save() в BookRepository.
     * Цель: убедиться, что книга сохраняется в базе данных и получает уникальный ID.
     */
    @Test
    public void testSaveBook() {
        // Шаг 1: Создаём новый объект Book для тестирования
        Book book = new Book();
        book.setTitle("Test Book");         // Устанавливаем название книги
        book.setAuthor("Test Author");      // Устанавливаем автора книги
        book.setPublicationYear(2023);      // Устанавливаем год публикации

        // Шаг 2: Сохраняем книгу в базе данных через репозиторий
        // Метод save() возвращает сохранённый объект с сгенерированным ID
        Book savedBook = bookRepository.save(book);

        // Шаг 3: Проверяем результаты
        // Убеждаемся, что ID не null (значит, книга успешно сохранена)
        assertNotNull(savedBook.getId(), "ID книги должен быть сгенерирован после сохранения");
        // Проверяем, что название книги осталось тем же
        assertEquals("Test Book", savedBook.getTitle(), "Название книги должно совпадать с заданным");
    }

    /**
     * Тест проверяет метод findById() в BookRepository.
     * Цель: убедиться, что книга находится по её ID после сохранения.
     */
    @Test
    public void testFindBookById() {
        // Шаг 1: Создаём и сохраняем книгу в базе данных
        Book book = new Book();
        book.setTitle("Find Me");
        book.setAuthor("Author");
        book.setPublicationYear(2020);
        bookRepository.save(book); // Сохраняем книгу, чтобы у неё появился ID

        // Шаг 2: Ищем книгу по её ID
        // Метод findById() возвращает Optional<Book>, который может быть пустым, если книга не найдена
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        // Шаг 3: Проверяем результаты
        // Убеждаемся, что Optional не пустой (книга найдена)
        assertTrue(foundBook.isPresent(), "Книга должна быть найдена по ID");
        // Проверяем, что найденная книга имеет ожидаемое название
        assertEquals("Find Me", foundBook.get().getTitle(), "Название найденной книги должно совпадать");
    }

    /**
     * Тест проверяет метод findAll() в BookRepository.
     * Цель: убедиться, что метод возвращает все сохранённые книги.
     */
    @Test
    public void testFindAllBooks() {
        // Шаг 1: Создаём и сохраняем первую книгу
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setPublicationYear(2021);
        bookRepository.save(book1);

        // Шаг 2: Создаём и сохраняем вторую книгу
        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setPublicationYear(2022);
        bookRepository.save(book2);

        // Шаг 3: Получаем список всех книг из базы
        List<Book> books = bookRepository.findAll();

        // Шаг 4: Проверяем, что список содержит ровно 2 книги
        assertEquals(2, books.size(), "Список должен содержать ровно 2 книги");
    }

    /**
     * Тест проверяет метод deleteById() в BookRepository.
     * Цель: убедиться, что книга удаляется из базы данных по её ID.
     */
    @Test
    public void testDeleteBookById() {
        // Шаг 1: Создаём и сохраняем книгу
        Book book = new Book();
        book.setTitle("Delete Me");
        book.setAuthor("Author");
        book.setPublicationYear(2019);
        bookRepository.save(book);

        // Шаг 2: Удаляем книгу по её ID
        bookRepository.deleteById(book.getId());

        // Шаг 3: Проверяем, что книга больше не существует в базе
        Optional<Book> deletedBook = bookRepository.findById(book.getId());
        assertFalse(deletedBook.isPresent(), "Книга не должна быть найдена после удаления");
    }

    // Тесты для PeopleRepository
    /**
     * Тест проверяет метод save() в PeopleRepository.
     * Цель: убедиться, что пользователь сохраняется в базе данных.
     */
    @Test
    public void testSavePersonSecurity() {
        // Шаг 1: Создаём объект пользователя
        PersonSecurity person = new PersonSecurity();
        person.setUsername("testuser");
        person.setPassword("password");
        person.setYearOfBirth(1990);
        person.setRole("ROLE_USER");

        // Шаг 2: Сохраняем пользователя
        PersonSecurity savedPerson = peopleRepository.save(person);

        // Шаг 3: Проверяем, что пользователь сохранён корректно
        assertNotNull(savedPerson.getId(), "ID пользователя должен быть сгенерирован");
        assertEquals("testuser", savedPerson.getUsername(), "Имя пользователя должно совпадать");
    }

    /**
     * Тест проверяет метод findById() в PeopleRepository.
     * Цель: убедиться, что пользователь находится по ID.
     */
    @Test
    public void testFindPersonSecurityById() {
        // Шаг 1: Создаём и сохраняем пользователя
        PersonSecurity person = new PersonSecurity();
        person.setUsername("findme");
        person.setPassword("pass");
        person.setYearOfBirth(1985);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        // Шаг 2: Ищем пользователя по ID
        Optional<PersonSecurity> foundPerson = peopleRepository.findById(person.getId());

        // Шаг 3: Проверяем, что пользователь найден
        assertTrue(foundPerson.isPresent(), "Пользователь должен быть найден по ID");
        assertEquals("findme", foundPerson.get().getUsername(), "Имя пользователя должно совпадать");
    }

    /**
     * Тест проверяет метод findAll() в PeopleRepository.
     * Цель: убедиться, что метод возвращает всех пользователей.
     */
    @Test
    public void testFindAllPersonSecurity() {
        // Шаг 1: Создаём и сохраняем первого пользователя
        PersonSecurity person1 = new PersonSecurity();
        person1.setUsername("user1");
        person1.setPassword("pass1");
        person1.setYearOfBirth(1995);
        person1.setRole("ROLE_USER");
        peopleRepository.save(person1);

        // Шаг 2: Создаём и сохраняем второго пользователя
        PersonSecurity person2 = new PersonSecurity();
        person2.setUsername("user2");
        person2.setPassword("pass2");
        person2.setYearOfBirth(1996);
        person2.setRole("ROLE_USER");
        peopleRepository.save(person2);

        // Шаг 3: Получаем список всех пользователей
        List<PersonSecurity> people = peopleRepository.findAll();

        // Шаг 4: Проверяем, что в списке 2 пользователя
        assertEquals(2, people.size(), "Список должен содержать 2 пользователей");
    }

    /**
     * Тест проверяет метод deleteById() в PeopleRepository.
     * Цель: убедиться, что пользователь удаляется из базы.
     */
    @Test
    public void testDeletePersonSecurityById() {
        // Шаг 1: Создаём и сохраняем пользователя
        PersonSecurity person = new PersonSecurity();
        person.setUsername("deleteuser");
        person.setPassword("pass");
        person.setYearOfBirth(1980);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        // Шаг 2: Удаляем пользователя по ID
        peopleRepository.deleteById(person.getId());

        // Шаг 3: Проверяем, что пользователь удалён
        Optional<PersonSecurity> deletedPerson = peopleRepository.findById(person.getId());
        assertFalse(deletedPerson.isPresent(), "Пользователь не должен быть найден после удаления");
    }

    /**
     * Тест проверяет кастомный метод findByUsername() в PeopleRepository.
     * Цель: убедиться, что пользователь находится по имени и не находится, если имени нет.
     */
    @Test
    public void testFindByUsername() {
        // Шаг 1: Создаём и сохраняем пользователя
        PersonSecurity person = new PersonSecurity();
        person.setUsername("uniqueuser");
        person.setPassword("pass");
        person.setYearOfBirth(1990);
        person.setRole("ROLE_USER");
        peopleRepository.save(person);

        // Шаг 2: Ищем пользователя по имени
        Optional<PersonSecurity> foundPerson = peopleRepository.findByUsername("uniqueuser");

        // Шаг 3: Проверяем, что пользователь найден
        assertTrue(foundPerson.isPresent(), "Пользователь должен быть найден по имени");
        assertEquals("uniqueuser", foundPerson.get().getUsername(), "Имя пользователя должно совпадать");

        // Шаг 4: Проверяем, что несуществующий пользователь не находится
        Optional<PersonSecurity> notFoundPerson = peopleRepository.findByUsername("nonexistent");
        assertFalse(notFoundPerson.isPresent(), "Несуществующий пользователь не должен быть найден");
    }

    // Тесты для PersonRepository
    /**
     * Тест проверяет метод save() в PersonRepository.
     * Цель: убедиться, что человек сохраняется в базе данных.
     */
    @Test
    public void testSavePerson() {
        // Шаг 1: Создаём объект человека
        Person person = new Person();
        person.setFullName("John Doe");
        person.setBirthYear(1990);

        // Шаг 2: Сохраняем человека
        Person savedPerson = personRepository.save(person);

        // Шаг 3: Проверяем, что человек сохранён
        assertNotNull(savedPerson.getId(), "ID человека должен быть сгенерирован");
        assertEquals("John Doe", savedPerson.getFullName(), "Имя человека должно совпадать");
    }

    /**
     * Тест проверяет метод findById() в PersonRepository.
     * Цель: убедиться, что человек находится по ID.
     */
    @Test
    public void testFindPersonById() {
        // Шаг 1: Создаём и сохраняем человека
        Person person = new Person();
        person.setFullName("Jane Doe");
        person.setBirthYear(1988);
        personRepository.save(person);

        // Шаг 2: Ищем человека по ID
        Optional<Person> foundPerson = personRepository.findById(person.getId());

        // Шаг 3: Проверяем, что человек найден
        assertTrue(foundPerson.isPresent(), "Человек должен быть найден по ID");
        assertEquals("Jane Doe", foundPerson.get().getFullName(), "Имя человека должно совпадать");
    }

    /**
     * Тест проверяет метод findAll() в PersonRepository.
     * Цель: убедиться, что метод возвращает всех людей.
     */
    @Test
    public void testFindAllPersons() {
        // Шаг 1: Создаём и сохраняем первого человека
        Person person1 = new Person();
        person1.setFullName("Person 1");
        person1.setBirthYear(1991);
        personRepository.save(person1);

        // Шаг 2: Создаём и сохраняем второго человека
        Person person2 = new Person();
        person2.setFullName("Person 2");
        person2.setBirthYear(1992);
        personRepository.save(person2);

        // Шаг 3: Получаем список всех людей
        List<Person> persons = personRepository.findAll();

        // Шаг 4: Проверяем, что в списке 2 человека
        assertEquals(2, persons.size(), "Список должен содержать 2 человека");
    }

    /**
     * Тест проверяет метод deleteById() в PersonRepository.
     * Цель: убедиться, что человек удаляется из базы.
     */
    @Test
    public void testDeletePersonById() {
        // Шаг 1: Создаём и сохраняем человека
        Person person = new Person();
        person.setFullName("Delete Me");
        person.setBirthYear(1987);
        personRepository.save(person);

        // Шаг 2: Удаляем человека по ID
        personRepository.deleteById(person.getId());

        // Шаг 3: Проверяем, что человек удалён
        Optional<Person> deletedPerson = personRepository.findById(person.getId());
        assertFalse(deletedPerson.isPresent(), "Человек не должен быть найден после удаления");
    }
}