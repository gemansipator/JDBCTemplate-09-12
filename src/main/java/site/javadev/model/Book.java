package site.javadev.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Person owner; // Владельцем книги становится объект Person

    private long id;          // Уникальный идентификатор книги

    @NotBlank(message = "Название книги не должно быть пустым")
    @Size(max = 255, message = "Название книги должно содержать не более 255 символов")
    private String title; // Название книги

    @NotBlank(message = "Имя автора не должно быть пустым")
    @Size(max = 255, message = "Имя автора должно содержать не более 255 символов")
    private String author; // Автор книги

    @Min(value = 1000, message = "Год издания должен быть не ранее 1000 года")
    @Max(value = 2100, message = "Год издания не должен быть позже 2100 года")
    private int year; // Год издания книги

    private long ownerId;     // ID владельца (человека), может быть null

}