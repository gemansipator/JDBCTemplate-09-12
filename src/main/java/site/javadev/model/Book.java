package site.javadev.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "year_of_production", nullable = false)
    private Integer yearOfProduction;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "annotation", columnDefinition = "TEXT")
    private String annotation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Column(name = "created_person", nullable = false, length = 100)
    private String createdPerson;

    @Column(name = "updated_person", length = 100)
    private String updatedPerson;

    @Column(name = "removed_person", length = 100)
    private String removedPerson;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person; // Связь с пользователем, который взял книгу
}