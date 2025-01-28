package site.javadev.Model;

import jakarta.persistence.*; // Импорт аннотаций для работы с JPA
import lombok.*; // Импорт аннотаций для генерации геттеров и сеттеров

import java.util.List; // Импорт коллекции для списка книг

@Getter // Генерация геттеров для всех полей
@Setter // Генерация сеттеров для всех полей
@Entity // Обозначает, что это сущность для JPA
@Table(name = "person") // Указывает название таблицы в базе данных
public class Person {

    @Id // Указывает, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения ключа
    @Column(name = "id") // Название столбца в базе данных
    private long id;

    @Column(name = "full_name", nullable = false, unique = true, length = 100) // Указывает столбец с ограничениями
    private String fullName; // Полное имя человека

    @Column(name = "birth_year", nullable = false) // Указывает столбец с ограничением на не-null значение
    private int birthYear; // Год рождения человека

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = false) // Связь "один ко многим" с сущностью Book
    private List<Book> books; // Список книг, принадлежащих человеку, связь с сущностью Book
}
