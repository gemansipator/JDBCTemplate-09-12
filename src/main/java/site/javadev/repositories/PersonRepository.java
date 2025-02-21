package site.javadev.repositories;

import site.javadev.model.Person; // Импорт модели Person
import org.springframework.data.jpa.repository.JpaRepository; // Импорт интерфейса для работы с JPA репозиториями
import org.springframework.stereotype.Repository; // Импорт аннотации для обозначения репозитория

@Repository // Обозначает данный интерфейс как репозиторий для работы с сущностью Person
public interface PersonRepository extends JpaRepository<Person, Long> { // Репозиторий для работы с сущностью Person
    // JpaRepository уже содержит основные методы для работы с данными, такие как save, findById, delete и т.д.
}
