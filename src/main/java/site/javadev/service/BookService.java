package site.javadev.service;

import org.springframework.stereotype.Service;
import site.javadev.model.Person;
import site.javadev.repositories.BookRepository;
import site.javadev.model.Book;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void assignBookToPerson(Long bookId, Person person) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            book.setOwner(person);
            bookRepository.save(book);
        }
    }

    public void removeBookFromPerson(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            book.setOwner(null);
            bookRepository.save(book);
        }
    }
}
