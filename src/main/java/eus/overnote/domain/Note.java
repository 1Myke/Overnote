package eus.overnote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
public class Note {
    @Id
    private UUID id;

    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    private final Date creationDate = new Date();

    @Column(nullable = false)
    private Date lastModificationDate = new Date();

    private Date deleteDate;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private OvernoteUser user;

/*
    @ManyToMany
    @JoinTable(
            name = "note_tags",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private final Set<Tag> tags = new HashSet<>();
*/
    @Column(nullable = false)
    private boolean deleted = false;

    // Empty constructor for JPA
    protected Note() {
    }

    // Complete constructor
    public Note(String title, String content, OvernoteUser user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.id = UUID.randomUUID();
    }

    public void setLastModificationDate(Date date) {
        getLastModificationDate().setTime(date.getTime());
    }

    public void moveToTrash() {
        this.deleted = true;
        this.deleteDate = new Date();
    }
}