package site.javadev.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.repositories.BookRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PersonService personService;
    private final FileStorageService fileStorageService;

    public BookService(BookRepository bookRepository, PersonService personService, FileStorageService fileStorageService) {
        this.bookRepository = bookRepository;
        this.personService = personService;
        this.fileStorageService = fileStorageService;
    }


    public List<Book> findAll() {
        return bookRepository.findByRemovedAtIsNull(); // Только активные книги
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public Book saveBook(Book book, MultipartFile coverImage) {
        if (coverImage != null && !coverImage.isEmpty()) {
            String coverImagePath = fileStorageService.storeFile(coverImage);
            book.setCoverImage(coverImagePath);
        }
        if (book.getCreatedAt() == null) {
            book.setCreatedAt(LocalDateTime.now());
        }
        if (book.getCreatedPerson() == null) {
            book.setCreatedPerson("system");
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        if (book.getCoverImage() != null) {
            String fileName = book.getCoverImage().substring("/uploads/covers/".length());
            fileStorageService.deleteFile(fileName);
        }
        bookRepository.delete(book); // Полное удаление
    }

    @Transactional
    public void softDeleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        book.setRemovedAt(LocalDateTime.now());
        book.setRemovedPerson("system");
        bookRepository.save(book); // Мягкое удаление
    }

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

    @Transactional
    public void looseBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        book.setOwner(null);
        bookRepository.save(book);
    }

    public List<Book> getAllDeletedBooks() {
        return bookRepository.findByRemovedAtIsNotNull(); // Список удалённых книг
    }

    @Transactional
    public void restoreBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        book.setRemovedAt(null);
        book.setRemovedPerson(null);
        bookRepository.save(book); // Восстановление
    }
}