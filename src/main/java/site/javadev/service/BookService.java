package site.javadev.service;

import org.springframework.transaction.annotation.Transactional;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PersonService personService;
    private final FileStorageService fileStorageService;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public Book saveBook(Book book, MultipartFile coverFile) {
        System.out.println("Saving book: " + book);
        if (coverFile != null && !coverFile.isEmpty()) {
            System.out.println("Processing cover file: " + coverFile.getOriginalFilename());
            String filePath = fileStorageService.storeFile(coverFile);
            book.setCoverImage(filePath);
            System.out.println("Cover file saved at: " + filePath);
        } else {
            System.out.println("No cover file provided");
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
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
}