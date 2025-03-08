package site.javadev.model;

import jakarta.persistence.*; // Импорт аннотаций для работы с JPA
import lombok.*; // Импорт аннотаций для генерации геттеров и сеттеров

@Getter // Генерация геттеров для всех полей
@Setter // Генерация сеттеров для всех полей
@Entity // Обозначает, что это сущность для JPA
@Table(name = "book") // Указывает название таблицы в базе данных
public class Book {

    @Id // Указывает, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения ключа
    @Column(name = "id") // Название столбца в базе данных
    private long id;

    @Column(nullable = false) // Указывает, что поле не может быть пустым
    private String title; // Название книги

    @Column(nullable = false) // Указывает, что поле не может быть пустым
    private String author; // Автор книги

    @Column(name = "year", nullable = false) // Переименовал year -> publication_year
    private int year;

    @ManyToOne // Указывает связь "многие к одному" с сущностью Person
    @JoinColumn(name = "owner_id") // Указывает, какой столбец в таблице связан с владельцем книги
    private Person owner; // Владелец книги, связь с сущностью Person
}
