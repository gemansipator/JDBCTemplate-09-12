package site.javadev.repositories;

import site.javadev.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.javadev.model.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
