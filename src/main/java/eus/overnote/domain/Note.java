package eus.overnote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    private Date creationDate = new Date();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private OvernoteUser user;

    // Empty constructor for JPA
    protected Note() {
    }

    // Complete constructor
    public Note(String title, String content, OvernoteUser user) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.user = user;
        this.creationDate = new Date();
    }
}