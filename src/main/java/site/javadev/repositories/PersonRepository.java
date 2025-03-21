package site.javadev.repositories;

import site.javadev.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // Выборка только активных записей
    List<Person> findByRemovedAtIsNull();
    // Выборка только удаленных записей
    List<Person> findByRemovedAtIsNotNull();
    Optional<Person> findByUsername(String username);
}