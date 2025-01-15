package site.javadev.dao;

import site.javadev.model.Books;
import site.javadev.model.Person;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Getter
@Repository
public class BooksDao {

    private static final Logger logger = LoggerFactory.getLogger(BooksDao.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BooksDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //получение всех книг
    public List<Books> getAllBooks() {
        try {
            String sql = "SELECT * FROM books";  // изменено на 'books'
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Books.class));
        } catch (Exception e) {
            logger.error("Ошибка при получении списка всех книг", e);
            throw new RuntimeException(e);
        }
    }

    public void saveBooks(@Valid Books books) {
        String sql = "INSERT INTO books (title, author, year, owner) VALUES (?, ?, ?, ?)";  // изменено на 'books'

        try {
            // Устанавливаем значение owner в NULL, если оно отсутствует
            jdbcTemplate.update(sql, books.getTitle(), books.getAuthor(), books.getYear(), null);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении объекта book: {}", books, e);
            throw new RuntimeException("Ошибка при сохранении объекта book", e);
        }
    }

    //получение объекта book по id
    public Books getBookById(long id) {
        try {
            String sql = "SELECT id, title, author, year, owner AS ownerId FROM books WHERE id = ?";  // изменено на 'books'

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Books book = new Books();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setYear(rs.getInt("year"));
                // Учитываем возможность NULL для owner
                Long ownerId = rs.getObject("ownerId", Long.class);
                book.setOwnerId(ownerId != null ? ownerId : 0L); // Если ownerId = null, задаем значение 0L
                return book;
            }, id);
        } catch (Exception e) {
            logger.error("Ошибка при получении объекта book с ID " + id, e);
            throw new RuntimeException("Ошибка при получении объекта book", e);
        }
    }

    //обновление объекта book
    public void updateBook(@Valid Books bookFromForm) {
        try {
            String sql = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";  // изменено на 'books'
            jdbcTemplate.update(sql, bookFromForm.getTitle(), bookFromForm.getAuthor(), bookFromForm.getYear(), bookFromForm.getId());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении объекта book", e);
            throw new RuntimeException("Ошибка при обновлении объекта book", e);
        }
    }

    //удаление объекта book
    public void deleteBook(long id) {
        try {
            String sql = "DELETE FROM books WHERE id = ?";  // изменено на 'books'
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении объекта book", e);
            throw new RuntimeException("Ошибка при удалении объекта book", e);
        }
    }

    // назначение книги читателю
    public void assignBookToPerson(long bookId, Person person) {
        try {
            String updateQuery = "UPDATE books SET owner = ? WHERE id = ?";  // изменено на 'books'
            int rowsAffected = jdbcTemplate.update(updateQuery, person.getId(), bookId);

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Книга с ID " + bookId + " не найдена");
            }
        } catch (Exception e) {
            logger.error("Ошибка при назначении книги", e);
            throw new RuntimeException("Ошибка при назначении книги", e);
        }
    }

    // удаляем книгу у читателя
    public void removeBookOwner(long bookId) {
        try {
            String sql = "UPDATE books SET owner = NULL WHERE id = ?";  // изменено на 'books'
            int rowsAffected = jdbcTemplate.update(sql, bookId);

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Книга с ID " + bookId + " не найдена");
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении связи книги с владельцем (ставим NULL)", e);
            throw new RuntimeException("Ошибка при удалении связи книги с владельцем", e);
        }
    }

}
