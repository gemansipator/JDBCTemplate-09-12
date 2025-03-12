package site.javadev.model;

import jakarta.persistence.*;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "year_of_production", nullable = false)
    private int yearOfProduction;

    @Column(name = "annotation", nullable = false)
    private String annotation = "No annotation";

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