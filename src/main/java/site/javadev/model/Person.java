package site.javadev.model;

import jakarta.persistence.*; // Импорт аннотаций для работы с JPA
import lombok.*; // Импорт аннотаций для генерации геттеров и сеттеров

import java.time.LocalDateTime;
import java.util.List; // Импорт коллекции для списка книг

@Getter // Генерация геттеров для всех полей
@Setter // Генерация сеттеров для всех полей
@Entity // Обозначает, что это сущность для JPA
@Table(name = "person") // Указывает название таблицы в базе данных
public class Person {

    @Id // Указывает, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения ключа
    private long id;

    @Column(name = "full_name", nullable = false, unique = true, length = 100)
    private String fullName;

    @Column(name = "birth_year", nullable = false)
    private int birthYear;

    @Column(name = "age", nullable = false)
    private Integer age = 0; // Значение по умолчанию

    @Column(name = "email", nullable = false, unique = true)
    private String email = "default@example.com"; // Значение по умолчанию

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber = "+1234567890"; // Значение по умолчанию

    @Column(name = "password", nullable = false)
    private String password = "defaultPassword"; // Значение по умолчанию

    @Column(name = "role", nullable = false)
    private String role = "ROLE_USER"; // Значение по умолчанию

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Текущее время

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "created_person", nullable = false)
    private String createdPerson = "system"; // Значение по умолчанию

    @Column(name = "removed_person")
    private String removedPerson;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = false) // Связь "один ко многим" с сущностью Book
    private List<Book> books; // Список книг, принадлежащих человеку, связь с сущностью Book
}
