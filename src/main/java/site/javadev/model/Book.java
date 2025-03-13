package site.javadev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Название не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "Автор не может быть пустым")
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull(message = "Год издания обязателен")
    @Column(name = "year_of_production", nullable = false)
    private int yearOfProduction;

    @NotEmpty(message = "Аннотация не может быть пустой")
    @Column(name = "annotation", nullable = false)
    private String annotation = "No annotation";

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "created_person", nullable = false)
    private String createdPerson = "system";

    @Column(name = "updated_person")
    private String updatedPerson;

    @Column(name = "removed_person")
    private String removedPerson;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;
}