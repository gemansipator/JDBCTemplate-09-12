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

@Controller // Указывает, что данный класс является контроллером Spring MVC
@RequestMapping("/books") // Все маршруты внутри этого контроллера начинаются с "/books"
@RequiredArgsConstructor // Автоматически генерирует конструктор для final-полей
public class BooksController {
    private final BookService bookService; // Сервис для работы с книгами
    private final PersonService personService; // Сервис для работы с пользователями

    // Получить список книг
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Доступ разрешен для пользователей с ролями USER или ADMIN
    @GetMapping
    public String getAllBooks(Model model) {
        try {
            List<Book> allBooks = bookService.getAllBooks(); // Получаем все книги из сервиса
            model.addAttribute("keyAllBooks", allBooks); // Добавляем список книг в модель для отображения
            return "books/view-with-all-books"; // Возвращаем имя представления для отображения списка книг
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке данных"); // В случае ошибки добавляем сообщение в модель
            return "books/error-view"; // Возвращаем имя представления для отображения ошибки
        }
    }

    // Создание книги (форма создания)
    @PreAuthorize("hasRole('ADMIN')") // Доступ разрешен только для пользователей с ролью ADMIN
    @GetMapping("/new")
    public String giveToUserPageToCreateNewBook(Model model) {
        model.addAttribute("keyOfNewBook", new Book()); // Добавляем пустой объект книги в модель для формы
        return "books/view-to-create-new-book"; // Возвращаем имя представления для формы создания книги
    }

    @GetMapping("/manage")     //на руках у пользователей
    public String manageBooksOnHand(Model model) {
        model.addAttribute("allBooks", bookService.getAllBooks());
        model.addAttribute("allPeople", personService.getAllPersons());
        return "books/manage-books-on-hand";
    }

    // Создание книги (обработка формы)
    @PreAuthorize("hasRole('ADMIN')") // Доступ разрешен только для пользователей с ролью ADMIN
    @PostMapping
    public String createBook(@ModelAttribute("keyOfNewBook") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-create-new-book";
        }
        try {
            // Заполнение обязательных полей, если они не заданы
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
        System.out.println("Fetching book with ID: " + id);
        Book bookById = bookService.getBookById(id);
        if (bookById == null) {
            System.out.println("Book not found with ID: " + id);
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        System.out.println("Book found: " + bookById);
        model.addAttribute("keyBookById", bookById);
        model.addAttribute("people", personService.getAllPersons());
        return "books/view-with-book-by-id";
    }

    // Редактирование книги (форма редактирования)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        System.out.println("Editing book with ID: " + id);
        Book bookToBeEdited = bookService.getBookById(id);
        if (bookToBeEdited == null) {
            System.out.println("Book not found with ID: " + id);
            model.addAttribute("errorMessage", "Книга не найдена");
            return "books/error-view";
        }
        System.out.println("Book found: " + bookToBeEdited);
        model.addAttribute("Book", bookToBeEdited);
        return "books/view-to-edit-book";
    }

    // Редактирование книги (обработка формы)
    @PreAuthorize("hasRole('ADMIN')") // Доступ разрешен только для пользователей с ролью ADMIN
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id,
                           @ModelAttribute("keyOfBookToBeEdited") @Valid Book bookFromForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/view-to-edit-book";
        }
        bookFromForm.setId(id);

        // Заполнение обязательных полей, если они не заданы
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
    @PreAuthorize("hasRole('ADMIN')") // Доступ разрешен только для пользователей с ролью ADMIN
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id); // Удаляем книгу через сервис
        return "redirect:/books"; // Перенаправляем на список книг
    }

    // Назначение книги читателю
    @PreAuthorize("hasRole('ADMIN')") // Доступ разрешен только для пользователей с ролью ADMIN
    @PostMapping("/assign/{id}")
    public String assignBook(@PathVariable("id") Long bookId, @RequestParam("personId") Long personId) {
        Person person = personService.getPersonById(personId); // Получаем пользователя по ID через сервис
        if (person != null) { // Проверяем, существует ли пользователь
            bookService.assignBookToPerson(bookId, person); // Назначаем книгу пользователю через сервис
        }
        return "redirect:/books/" + bookId; // Перенаправляем на страницу книги
    }

    // Снятие книги с читателя
    @PreAuthorize("hasRole('ADMIN')") // Доступ разрешен только для пользователей с ролью ADMIN
    @PostMapping("/loose/{id}")
    public String looseBook(@PathVariable("id") Long bookId) {
        bookService.removeBookFromPerson(bookId); // Снимаем книгу с пользователя через сервис
        return "redirect:/books/" + bookId; // Перенаправляем на страницу книги
    }
}
