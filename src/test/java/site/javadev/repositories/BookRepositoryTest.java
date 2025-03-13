//package site.javadev.repositories;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import site.javadev.model.Book;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class BookRepositoryTest {
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Test
//    public void testSaveBook() {
//        Book book = new Book();
//        book.setTitle("Test Book");
//        book.setAuthor("Test Author");
//        book.setYear(2023); // Используйте setYear вместо setPublicationYear
//        book.setAnnotation("Test Annotation");
//
//        Book savedBook = bookRepository.save(book);
//
//        assertNotNull(savedBook.getId());
//        assertEquals("Test Book", savedBook.getTitle());
//        assertEquals("Test Author", savedBook.getAuthor());
//        assertEquals(2023, savedBook.getYear());
//        assertEquals("Test Annotation", savedBook.getAnnotation());
//    }
//
//    @Test
//    public void testFindBookById() {
//        Book book = new Book();
//        book.setTitle("Test Book");
//        book.setAuthor("Test Author");
//        book.setYear(2023); // Используйте setYear вместо setPublicationYear
//        book.setAnnotation("Test Annotation");
//
//        Book savedBook = bookRepository.save(book);
//        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);
//
//        assertNotNull(foundBook);
//        assertEquals("Test Book", foundBook.getTitle());
//        assertEquals("Test Author", foundBook.getAuthor());
//        assertEquals(2023, foundBook.getYear());
//        assertEquals("Test Annotation", foundBook.getAnnotation());
//    }
//
//    @Test
//    public void testUpdateBook() {
//        Book book = new Book();
//        book.setTitle("Test Book");
//        book.setAuthor("Test Author");
//        book.setYear(2023); // Используйте setYear вместо setPublicationYear
//        book.setAnnotation("Test Annotation");
//
//        Book savedBook = bookRepository.save(book);
//        savedBook.setTitle("Updated Title");
//        savedBook.setYear(2024); // Используйте setYear вместо setPublicationYear
//
//        Book updatedBook = bookRepository.save(savedBook);
//
//        assertEquals("Updated Title", updatedBook.getTitle());
//        assertEquals(2024, updatedBook.getYear());
//    }
//
//    @Test
//    public void testDeleteBook() {
//        Book book = new Book();
//        book.setTitle("Test Book");
//        book.setAuthor("Test Author");
//        book.setYear(2023); // Используйте setYear вместо setPublicationYear
//        book.setAnnotation("Test Annotation");
//
//        Book savedBook = bookRepository.save(book);
//        bookRepository.delete(savedBook);
//
//        assertFalse(bookRepository.findById(savedBook.getId()).isPresent());
//    }
//}