package site.javadev.model;

import jakarta.persistence.*; // Импорт аннотаций для работы с JPA
import jakarta.validation.constraints.NotEmpty; // Импорт аннотации для проверки на пустоту
import jakarta.validation.constraints.Size; // Импорт аннотации для проверки длины строки
import lombok.AllArgsConstructor; // Импорт аннотации для генерации конструктора с параметрами
import lombok.Getter; // Импорт аннотации для генерации геттеров
import lombok.NoArgsConstructor; // Импорт аннотации для генерации конструктора без параметров
import lombok.Setter; // Импорт аннотации для генерации сеттеров

@Getter // Генерация геттеров для всех полей
@Setter // Генерация сеттеров для всех полей
@AllArgsConstructor // Генерация конструктора с параметрами для всех полей
@NoArgsConstructor // Генерация конструктора без параметров
@Entity // Обозначает, что это сущность для JPA
@Table(name = "person_security") // Указывает название таблицы в базе данных
public class PersonSecurity {

    @Id // Указывает, что это первичный ключ
    @Column(name = "id") // Указывает название столбца в базе данных
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения ключа
    private Long id; // Идентификатор

    @NotEmpty(message = "Поле не может быть пустым") // Проверка на пустое значение
    @Size(min = 2, max = 20, message = "Поле должно быть от 2 до 20 символов") // Проверка на размер строки
    @Column(name = "username") // Название столбца в zбазе данных
    private String username; // Имя пользователя

    // @NotEmpty(message = "Поле не может быть пустым") // Проверка на пустое значение
    // @Size(min = 4, message = "Значение должно быть от 4 символов") // Проверка на минимальную длину
    @Column(name = "year_of_birth") // Название столбца в базе данных
    private Integer yearOfBirth; // Год рождения пользователя

    @Column(name = "password") // Название столбца в базе данных
    private String password; // Пароль пользователя

    @Column(name = "role") // Название столбца в базе данных
    private String role; // Роль пользователя
}
