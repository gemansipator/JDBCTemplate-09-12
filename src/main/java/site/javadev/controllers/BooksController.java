package site.javadev.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.javadev.model.Book;
import site.javadev.model.Person;
import site.javadev.service.BookService;
import site.javadev.service.PersonService;

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
        List<Book> allBooks = bookService.findAll();
        model.addAttribute("keyAllBooks", allBooks);
        return "books/view-with-all-books";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Доступ для USER и ADMIN
    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model) {
        Book bookById = bookService.getBookById(id);
        if (bookById == null) {
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        model.addAttribute("keyBookById", bookById);
        model.addAttribute("people", personService.getAllPersons()); // Оставляем для админов
        return "books/view-with-book-by-id";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String giveToUserPageToCreateNewBook(Model model) {
        model.addAttribute("keyOfNewBook", new Book());
        return "books/view-to-create-new-book";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createBook(@ModelAttribute("keyOfNewBook") @Valid Book book,
                             BindingResult bindingResult,
                             @RequestParam("coverFile") MultipartFile coverFile) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-create-new-book";
        }
        bookService.saveBook(book, coverFile);
        return "redirect:/books";
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
        bookService.saveBook(bookFromForm, coverFile);
        return "redirect:/books";
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/my-books")
    public String getMyBooks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        System.out.println("DEBUG: Entering /my-books for user: " + userDetails.getUsername());
        Person currentUser = personService.getPersonByUsername(userDetails.getUsername());
        if (currentUser == null) {
            System.out.println("DEBUG: Current user not found for username: " + userDetails.getUsername());
            return "redirect:/auth/login?error=userNotFound";
        }
        List<Book> myBooks = bookService.getBooksByOwner(currentUser.getId());
        System.out.println("DEBUG: Found " + myBooks.size() + " books for user " + currentUser.getUsername());
        model.addAttribute("myBooks", myBooks);
        System.out.println("DEBUG: Returning template books/my-books");
        return "books/my-books";
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/manage")
    public String manageBooksOnHand(Model model) {
        model.addAttribute("allBooks", bookService.findAll());
        model.addAttribute("allPeople", personService.getAllPersons());
        return "books/manage-books-on-hand";
    }


    @PreAuthorize("hasRole('ADMIN')") // Только админы назначают любому человеку
    @PostMapping("/assign/{id}")
    public String assignBook(@PathVariable("id") Long bookId, @RequestParam("personId") Long personId) {
        bookService.assignBook(bookId, personId);
        return "redirect:/books/manage";
    }
    @PreAuthorize("hasRole('USER')") // Пользователи берут себе
    @PostMapping("/take/{id}")
    public String takeBook(@PathVariable("id") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        Person currentUser = personService.getPersonByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return "redirect:/books/manage?error=userNotFound";
        }
        bookService.assignBook(bookId, currentUser.getId());
        return "redirect:/books/manage";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/loose/{id}")
    public String looseBook(@PathVariable("id") Long bookId) {
        bookService.looseBook(bookId);
        return "redirect:/books/manage";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/soft-delete/{id}")
    public String softDeleteBook(@PathVariable("id") Long id) {
        bookService.softDeleteBook(id);
        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleted")
    public String getAllDeletedBooks(Model model) {
        List<Book> deletedBooks = bookService.getAllDeletedBooks();
        model.addAttribute("keyAllDeletedBooks", deletedBooks);
        return "books/view-with-deleted-books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/restore/{id}")
    public String restoreBook(@PathVariable("id") Long id) {
        bookService.restoreBook(id);
        return "redirect:/books/deleted";
    }
}