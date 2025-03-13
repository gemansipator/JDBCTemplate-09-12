package site.javadev;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import site.javadev.repositories.BookRepository;
import site.javadev.repositories.PersonRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test") // Используем профиль test
class ApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void contextLoads() {
        assertNotNull(bookRepository);
        assertNotNull(personRepository);
    }
}