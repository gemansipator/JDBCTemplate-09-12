package site.javadev.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import site.javadev.model.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Book Repository Test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Проверка сохранения книги")
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

    @DisplayName("Проверка поиска книги по id")
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

    @DisplayName("Проверка поиска всех книг")
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

    @DisplayName("Проверка удаления книги по id")
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
}
