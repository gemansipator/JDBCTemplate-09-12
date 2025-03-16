package site.javadev.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.javadev.model.Book;
import site.javadev.model.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Book Repository Test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PersonRepository personRepository;

    private Person owner;

    @BeforeEach
    public void setUp() {
        // Создаём и сохраняем владельца книги
        owner = new Person();
        owner.setName("Owner");
        owner.setAge(30);
        owner.setEmail("owner@example.com");
        owner.setPhoneNumber("+123");
        owner.setPassword("pass");
        owner.setRole("ROLE_USER");
        owner.setUsername("owner");
        owner.setCreatedAt(LocalDateTime.now());
        owner.setCreatedPerson("admin"); // Добавлено обязательное поле
        personRepository.save(owner);
    }

    @Test
    @DisplayName("Проверка сохранения книги")
    public void testSaveBook() {
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setYearOfProduction(2023);
        book.setAnnotation("Test Annotation"); // Указано значение
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedPerson("admin");

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getName());
        assertEquals("Test Author", savedBook.getAuthor());
        assertEquals(2023, savedBook.getYearOfProduction());
        assertEquals("Test Annotation", savedBook.getAnnotation());
        assertEquals("admin", savedBook.getCreatedPerson());
    }

    @Test
    @DisplayName("Проверка поиска книги по ID")
    public void testFindBookById() {
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setYearOfProduction(2023);
        book.setAnnotation("Test Annotation"); // Указано значение
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedPerson("admin");

        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getName());
        assertEquals("Test Author", foundBook.get().getAuthor());
        assertEquals(2023, foundBook.get().getYearOfProduction());
    }

    @Test
    @DisplayName("Проверка обновления книги")
    public void testUpdateBook() {
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setYearOfProduction(2023);
        book.setAnnotation("Test Annotation"); // Указано значение
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedPerson("admin");

        Book savedBook = bookRepository.save(book);
        savedBook.setName("Updated Book");
        savedBook.setYearOfProduction(2024);
        savedBook.setUpdatedAt(LocalDateTime.now());
        savedBook.setUpdatedPerson("admin");

        Book updatedBook = bookRepository.save(savedBook);

        assertEquals("Updated Book", updatedBook.getName());
        assertEquals(2024, updatedBook.getYearOfProduction());
        assertNotNull(updatedBook.getUpdatedAt());
        assertEquals("admin", updatedBook.getUpdatedPerson());
    }

    @Test
    @DisplayName("Проверка мягкого удаления книги")
    public void testSoftDeleteBook() {
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setYearOfProduction(2023);
        book.setAnnotation("Test Annotation"); // Указано значение
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedPerson("admin");

        Book savedBook = bookRepository.save(book);
        savedBook.setRemovedAt(LocalDateTime.now());
        savedBook.setRemovedPerson("admin");
        bookRepository.save(savedBook);

        Optional<Book> removedBook = bookRepository.findById(savedBook.getId());
        assertTrue(removedBook.isPresent());
        assertNotNull(removedBook.get().getRemovedAt());
        assertEquals("admin", removedBook.get().getRemovedPerson());
    }

    @Test
    @DisplayName("Проверка поиска книг по владельцу")
    public void testFindByOwnerId() {
        Book book = new Book();
        book.setName("Test Book");
        book.setAuthor("Test Author");
        book.setYearOfProduction(2023);
        book.setAnnotation("Test Annotation"); // Указано значение
        book.setOwner(owner); // Устанавливаем владельца
        book.setCreatedAt(LocalDateTime.now());
        book.setCreatedPerson("admin"); // Указано значение
        bookRepository.save(book);

        List<Book> books = bookRepository.findByOwnerIdAndRemovedAtIsNull(owner.getId());
        assertFalse(books.isEmpty());
        assertEquals("Test Book", books.get(0).getName());
        assertEquals(owner.getId(), books.get(0).getOwner().getId());
    }

    @Test
    @DisplayName("Проверка поиска всех книг")
    public void testFindAllBooks() {
        Book book1 = new Book();
        book1.setName("Book 1");
        book1.setAuthor("Author 1");
        book1.setYearOfProduction(2021);
        book1.setAnnotation("Annotation 1"); // Указано значение
        book1.setCreatedAt(LocalDateTime.now());
        book1.setCreatedPerson("admin"); // Указано значение
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setName("Book 2");
        book2.setAuthor("Author 2");
        book2.setYearOfProduction(2022);
        book2.setAnnotation("Annotation 2"); // Указано значение
        book2.setCreatedAt(LocalDateTime.now());
        book2.setCreatedPerson("admin"); // Указано значение
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();
        assertEquals(2, books.size());
    }
}