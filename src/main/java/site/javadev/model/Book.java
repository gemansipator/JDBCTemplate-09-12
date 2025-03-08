package site.javadev.model;

import jakarta.persistence.*; // Импорт аннотаций для работы с JPA
import lombok.*; // Импорт аннотаций для генерации геттеров и сеттеров

import java.time.LocalDateTime;

@Getter // Генерация геттеров для всех полей
@Setter // Генерация сеттеров для всех полей
@Entity // Обозначает, что это сущность для JPA
@Table(name = "book") // Указывает название таблицы в базе данных
public class Book {

    @Id // Указывает, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения ключа
    @Column(name = "id") // Название столбца в базе данных
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "annotation", nullable = false)
    private String annotation = "No annotation"; // Значение по умолчанию

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Текущее время

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "created_person", nullable = false)
    private String createdPerson = "system"; // Значение по умолчанию

    @Column(name = "updated_person")
    private String updatedPerson;

    @Column(name = "removed_person")
    private String removedPerson;

    @ManyToOne // Указывает связь "многие к одному" с сущностью Person
    @JoinColumn(name = "owner_id") // Указывает, какой столбец в таблице связан с владельцем книги
    private Person owner; // Владелец книги, связь с сущностью Person
}
