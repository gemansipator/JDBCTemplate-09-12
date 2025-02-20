package site.javadev.repositories;

import site.javadev.model.Book; // Импорт модели Book
import org.springframework.data.jpa.repository.JpaRepository; // Импорт интерфейса для работы с JPA репозиториями
import org.springframework.stereotype.Repository; // Импорт аннотации для обозначения репозитория

@Repository // Обозначает, что данный интерфейс является репозиторием для Spring Data JPA
public interface BookRepository extends JpaRepository<Book, Long> { // Репозиторий для работы с сущностью Book
    // При необходимости можно добавить кастомные запросы
}
