package site.javadev.service;

import site.javadev.Model.Book; // Импорт модели Book для работы с книгами
import site.javadev.Model.Person; // Импорт модели Person для работы с людьми
import site.javadev.repositories.BookRepository; // Импорт репозитория BookRepository для работы с базой данных
import org.springframework.stereotype.Service; // Импорт аннотации Service для пометки класса как сервиса

import java.util.List; // Импорт класса List для работы с коллекциями

@Service // Аннотация, которая помечает класс как сервис и позволяет Spring управлять его жизненным циклом
public class BookService {

    private final BookRepository bookRepository; // Репозиторий для работы с книгами

    // Конструктор для внедрения зависимости репозитория
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Получение всех книг из базы данных
    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // Возвращаем список всех книг
    }

    // Получение книги по ее ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null); // Ищем книгу по ID, если не найдена - возвращаем null
    }

    // Сохранение новой книги или обновление существующей
    public Book saveBook(Book book) {
        return bookRepository.save(book); // Сохраняем или обновляем книгу в базе данных
    }

    // Удаление книги по ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id); // Удаляем книгу из базы данных по ID
    }

    // Присваивание книги конкретному человеку
    public void assignBookToPerson(Long bookId, Person person) {
        Book book = bookRepository.findById(bookId).orElse(null); // Ищем книгу по ID
        if (book != null) {
            book.setOwner(person); // Присваиваем книгу владельцу
            bookRepository.save(book); // Сохраняем изменения в базе данных
        }
    }

    // Удаление книги у человека (снятие владельца)
    public void removeBookFromPerson(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null); // Ищем книгу по ID
        if (book != null) {
            book.setOwner(null); // Убираем владельца книги
            bookRepository.save(book); // Сохраняем изменения в базе данных
        }
    }
}
