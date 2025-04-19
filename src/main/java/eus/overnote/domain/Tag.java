package eus.overnote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Setter
    private String name;

    @ManyToMany
    private Set<Note> notes;

    // Empty constructor for JPA
    protected Tag() {
    }

    // Complete constructor
    public Tag(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}