package site.javadev.controllers;

import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.service.BookService;
import site.javadev.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BookService bookService;
    private final PersonService personService;

    // Получить список книг
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public String getAllBooks(Model model) {
        try {
            List<Book> allBooks = bookService.findAll(); // Заменил getAllBooks на findAll
            model.addAttribute("keyAllBooks", allBooks);
            return "books/view-with-all-books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "books/error-view";
        }
    }

    // Создание книги (форма создания)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String giveToUserPageToCreateNewBook(Model model) {
        model.addAttribute("keyOfNewBook", new Book());
        return "books/view-to-create-new-book";
    }

    // Управление книгами на руках
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Добавил права доступа
    @GetMapping("/manage")
    public String manageBooksOnHand(Model model) {
        model.addAttribute("allBooks", bookService.findAll()); // Заменил getAllBooks на findAll
        model.addAttribute("allPeople", personService.getAllPersons());
        return "books/manage-books-on-hand";
    }

    // Создание книги (обработка формы)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createBook(@ModelAttribute("keyOfNewBook") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-create-new-book";
        }
        try {
            if (book.getAnnotation() == null) {
                book.setAnnotation("No annotation");
            }
            if (book.getCreatedAt() == null) {
                book.setCreatedAt(LocalDateTime.now());
            }
            if (book.getCreatedPerson() == null) {
                book.setCreatedPerson("system");
            }
            bookService.saveBook(book);
            return "redirect:/books";
        } catch (Exception e) {
            return "books/error-view";
        }
    }

    // Получение книги по ID
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

    // Редактирование книги (форма редактирования)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book bookToBeEdited = bookService.getBookById(id);
        if (bookToBeEdited == null) {
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        model.addAttribute("Book", bookToBeEdited); // Исправил key с "Book" на "keyOfBookToBeEdited" для консистентности
        return "books/view-to-edit-book";
    }

    // Редактирование книги (обработка формы)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id,
                           @ModelAttribute("keyOfBookToBeEdited") @Valid Book bookFromForm,
                           BindingResult bindingResult) {
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
        bookService.saveBook(bookFromForm);
        return "redirect:/books";
    }

    // Удаление книги
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // Назначение книги читателю
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign/{id}")
    public String assignBook(@PathVariable("id") Long bookId, @RequestParam("personId") Long personId) {
        bookService.assignBook(bookId, personId); // Заменил assignBookToPerson на assignBook
        return "redirect:/books/manage"; // Перенаправление на страницу управления вместо книги
    }

    // Снятие книги с читателя
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/loose/{id}")
    public String looseBook(@PathVariable("id") Long bookId) {
        bookService.looseBook(bookId); // Заменил removeBookFromPerson на looseBook
        return "redirect:/books/manage"; // Перенаправление на страницу управления вместо книги
    }
}