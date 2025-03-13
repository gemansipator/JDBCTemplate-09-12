package site.javadev.repositories;

import site.javadev.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByName(String name);
    Optional<Person> findByEmail(String email);
}