package site.javadev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @NotEmpty(message = "Поле не может быть пустым")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Поле не может быть пустым")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_person", nullable = false)
    private String createdPerson;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "removed_person")
    private String removedPerson;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Book> books;
}