package site.javadev.service;

import org.springframework.transaction.annotation.Transactional;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.repositories.BookRepository; // Исправлен импорт
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PersonService personService;

    // Получение всех книг
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // Получение книги по ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Сохранение книги
    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Удаление книги по ID
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Назначение книги человеку
    @Transactional
    public void assignBook(Long bookId, Long personId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        Person person = personService.getPersonById(personId);
        if (person == null) {
            throw new IllegalArgumentException("Person not found: " + personId);
        }
        book.setOwner(person);
        bookRepository.save(book);
    }

    // Освобождение книги
    @Transactional
    public void looseBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        book.setOwner(null);
        bookRepository.save(book);
    }
}