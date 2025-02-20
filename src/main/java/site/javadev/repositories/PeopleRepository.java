package site.javadev.repositories;

import site.javadev.Model.PersonSecurity; // Импорт модели PersonSecurity
import org.springframework.data.jpa.repository.JpaRepository; // Импорт интерфейса для работы с JPA репозиториями
import org.springframework.stereotype.Repository; // Импорт аннотации для обозначения репозитория

import java.util.Optional; // Импорт Optional для работы с возможным отсутствием значения

@Repository // Обозначает, что данный интерфейс является репозиторием для Spring Data JPA
public interface PeopleRepository extends JpaRepository<PersonSecurity, Long> { // Репозиторий для работы с сущностью PersonSecurity
    // Метод для поиска пользователя по имени
    Optional<PersonSecurity> findByUsername(String userName);
}
