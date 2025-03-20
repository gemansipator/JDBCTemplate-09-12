package site.javadev.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.repositories.BookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PersonService personService;
    private final FileStorageService fileStorageService;

    @Transactional
    public List<Book> getBooksByOwner(Long personId) {
        return bookRepository.findByOwnerIdAndRemovedAtIsNull(personId);
    }

    @Transactional
    public List<Book> findAll() {
        return bookRepository.findByRemovedAtIsNull();
    }

    @Transactional
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
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
        bookRepository.delete(book);
    }

    @Transactional
    public boolean softDeleteBook(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
            book.setRemovedAt(LocalDateTime.now());
            book.setRemovedPerson("system");
            bookRepository.save(book);
            log.info("Book with ID {} was soft deleted successfully", id);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Failed to soft delete book with ID {}: {}", id, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error while soft deleting book with ID {}: {}", id, e.getMessage());
            return false;
        }
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
        return bookRepository.findByRemovedAtIsNotNull();
    }

    @Transactional
    public void restoreBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
        book.setRemovedAt(null);
        book.setRemovedPerson(null);
        bookRepository.save(book);
    }
}