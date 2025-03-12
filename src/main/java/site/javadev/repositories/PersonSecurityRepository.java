package site.javadev.repositories;

import site.javadev.model.PersonSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonSecurityRepository extends JpaRepository<PersonSecurity, Long> {
    Optional<PersonSecurity> findByUsername(String username);
}