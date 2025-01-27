package site.javadev.Controllers;

import site.javadev.Model.Book;
import site.javadev.Model.Person;
import site.javadev.service.BookService;
import site.javadev.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
            List<Book> allBooks = bookService.getAllBooks();
            model.addAttribute("keyAllBooks", allBooks);
            return "books/view-with-all-books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных");
            return "books/error-view";
        }
    }

    // Создание книги GET
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String giveToUserPageToCreateNewBook(Model model) {
        model.addAttribute("keyOfNewBook", new Book());
        return "books/view-to-create-new-book";
    }

    // Создание книги POST
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createBook(@ModelAttribute("keyOfNewBook") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-create-new-book";
        }
        try {
            bookService.saveBook(book);
            return "redirect:/books";
        } catch (Exception e) {
            return "books/error-view";
        }
    }

    // Получение книги по ID GET
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

    // Редактирование книги по ID GET
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        Book bookToBeEdited = bookService.getBookById(id);
        if (bookToBeEdited == null) {
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        model.addAttribute("Book", bookToBeEdited);
        return "books/view-to-edit-book";
    }

    // Редактирование книги по ID POST
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id,
                           @ModelAttribute("keyOfBookToBeEdited") @Valid Book bookFromForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-edit-book";
        }
        bookFromForm.setId(id);
        bookService.saveBook(bookFromForm);
        return "redirect:/books";
    }

    // Удаление книги по ID DELETE
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
        Person person = personService.getPersonById(personId);
        if (person != null) {
            bookService.assignBookToPerson(bookId, person);
        }
        return "redirect:/books/" + bookId;
    }

    // Удаляем книгу у читателя
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/loose/{id}")
    public String looseBook(@PathVariable("id") Long bookId) {
        bookService.removeBookFromPerson(bookId);
        return "redirect:/books/" + bookId;
    }
}
