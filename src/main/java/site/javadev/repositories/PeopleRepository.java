package site.javadev.repositories;

import site.javadev.model.PersonSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<PersonSecurity, Long> {
    Optional<PersonSecurity> findByUsername(String userName);

}
