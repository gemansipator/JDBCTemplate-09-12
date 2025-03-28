package site.javadev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.javadev.model.Book;
import site.javadev.service.BookService;
import site.javadev.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;
    private final FileStorageService fileStorageService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> createBook(
            @RequestPart("book") Map<String, Object> bookData,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        Book book = new Book();
        book.setName((String) bookData.get("name"));
        book.setAuthor((String) bookData.get("author"));
        book.setYearOfProduction((Integer) bookData.get("yearOfProduction"));
        book.setAnnotation((String) bookData.get("annotation"));

        Book savedBook = bookService.saveBook(book, coverImage);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestPart("book") Map<String, Object> bookData,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        Book book = new Book();
        book.setId(id);
        book.setName((String) bookData.get("name"));
        book.setAuthor((String) bookData.get("author"));
        book.setYearOfProduction((Integer) bookData.get("yearOfProduction"));
        book.setAnnotation((String) bookData.get("annotation"));

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
        Optional<Book> book = bookService.getBookById(id);
        if (book.isEmpty() || book.get().getCoverImage() == null) {
            return ResponseEntity.notFound().build();
        }
        String fileName = book.get().getCoverImage().substring("/uploads/covers/".length());
        Path filePath = fileStorageService.getFilePath(fileName);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            String contentType = fileName.endsWith(".png") ? MediaType.IMAGE_PNG_VALUE :
                    fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ? MediaType.IMAGE_JPEG_VALUE :
                            MediaType.APPLICATION_OCTET_STREAM_VALUE;
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}