package site.javadev.repositories;

import site.javadev.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // При необходимости можно добавить кастомные запросы
}
