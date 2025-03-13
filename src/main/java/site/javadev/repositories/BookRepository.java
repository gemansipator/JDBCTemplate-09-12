package site.javadev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javadev.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByRemovedAtIsNull(); // Только активные книги
    List<Book> findByRemovedAtIsNotNull(); // Только удалённые книги
}