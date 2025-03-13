package site.javadev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javadev.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}