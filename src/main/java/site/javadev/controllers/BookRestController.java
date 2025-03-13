package site.javadev.controllers;

import site.javadev.model.Book;
import site.javadev.service.BookService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.javadev.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;
    private final FileStorageService fileStorageService; // Добавляем зависимость

    public BookRestController(BookService bookService, FileStorageService fileStorageService) {
        this.bookService = bookService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> createBook(@RequestPart("book") Book book,
                                           @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        Book savedBook = bookService.saveBook(book, coverImage);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> updateBook(@PathVariable Long id,
                                           @RequestPart("book") Book book,
                                           @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        book.setId(id);
        Book updatedBook = bookService.saveBook(book, coverImage);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cover")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> getBookCover(@PathVariable Long id) throws IOException {
        Book book = bookService.getBookById(id);
        if (book == null || book.getCoverImage() == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = fileStorageService.getFilePath(book.getCoverImage().substring("/static/covers/".length()));
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Предполагаем JPEG, можно уточнить по расширению
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}