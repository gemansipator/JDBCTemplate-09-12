package site.javadev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @NotEmpty(message = "Поле не может быть пустым")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Поле не может быть пустым")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 20, message = "Имя пользователя должно быть от 2 до 20 символов")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 4, message = "Пароль должен быть от 4 символов")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role = "ROLE_USER";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "created_person", nullable = false)
    private String createdPerson = "system";

    @Column(name = "removed_person")
    private String removedPerson;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;
}