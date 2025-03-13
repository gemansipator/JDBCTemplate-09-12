package site.javadev.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.service.BookService;
import site.javadev.service.PersonService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;
    private final PersonService personService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public String getAllBooks(Model model) {
        try {
            List<Book> allBooks = bookService.findAll();
            model.addAttribute("keyAllBooks", allBooks);
            return "books/view-with-all-books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "books/error-view";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String giveToUserPageToCreateNewBook(Model model) {
        model.addAttribute("keyOfNewBook", new Book());
        return "books/view-to-create-new-book";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/manage")
    public String manageBooksOnHand(Model model) {
        model.addAttribute("allBooks", bookService.findAll());
        model.addAttribute("allPeople", personService.getAllPersons());
        return "books/manage-books-on-hand";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createBook(@ModelAttribute("keyOfNewBook") @Valid Book book,
                             BindingResult bindingResult,
                             @RequestParam("coverFile") MultipartFile coverFile) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "books/view-to-create-new-book";
        }
        try {
            System.out.println("Creating book: " + book);
            System.out.println("Cover file: " + (coverFile.isEmpty() ? "empty" : coverFile.getOriginalFilename()));
            if (book.getAnnotation() == null) {
                book.setAnnotation("No annotation");
            }
            if (book.getCreatedAt() == null) {
                book.setCreatedAt(LocalDateTime.now());
            }
            if (book.getCreatedPerson() == null) {
                book.setCreatedPerson("system");
            }
            bookService.saveBook(book, coverFile);
            return "redirect:/books";
        } catch (Exception e) {
            e.printStackTrace();
            return "books/error-view";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model) {
        Book bookById = bookService.getBookById(id);
        if (bookById == null) {
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        model.addAttribute("keyBookById", bookById);
        model.addAttribute("people", personService.getAllPersons());
        return "books/view-with-book-by-id";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book bookToBeEdited = bookService.getBookById(id);
        if (bookToBeEdited == null) {
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        model.addAttribute("keyOfBookToBeEdited", bookToBeEdited);
        return "books/view-to-edit-book";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id,
                           @ModelAttribute("keyOfBookToBeEdited") @Valid Book bookFromForm,
                           BindingResult bindingResult,
                           @RequestParam(value = "coverFile", required = false) MultipartFile coverFile) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-edit-book";
        }
        bookFromForm.setId(id);
        if (bookFromForm.getAnnotation() == null) {
            bookFromForm.setAnnotation("No annotation");
        }
        if (bookFromForm.getCreatedAt() == null) {
            bookFromForm.setCreatedAt(LocalDateTime.now());
        }
        if (bookFromForm.getCreatedPerson() == null) {
            bookFromForm.setCreatedPerson("system");
        }
        bookService.saveBook(bookFromForm, coverFile);
        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign/{id}")
    public String assignBook(@PathVariable("id") Long bookId, @RequestParam("personId") Long personId) {
        bookService.assignBook(bookId, personId);
        return "redirect:/books/manage";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/loose/{id}")
    public String looseBook(@PathVariable("id") Long bookId) {
        bookService.looseBook(bookId);
        return "redirect:/books/manage";
    }
}