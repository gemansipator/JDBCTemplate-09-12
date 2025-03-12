package site.javadev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person_security")
public class PersonSecurity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 20, message = "Имя должно быть от 2 до 20 символов")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

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
}