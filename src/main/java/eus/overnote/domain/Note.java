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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Setter
    private String title;

    @Lob
    @Column(nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    private final Date creationDate = new Date();

    @Column(nullable = false)
    private final Date lastModificationDate = new Date();

    private Date deleteDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private OvernoteUser user;

    @ManyToMany
    @JoinColumn(nullable = false)
    private final Set<Tag> tags = new HashSet<>();

    @Column(nullable = false)
    private boolean deleted = false;

    // Empty constructor for JPA
    protected Note() {
    }

    // Complete constructor
    public Note(String title, String content, OvernoteUser user) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void setLastModificationDate(Date date) {
        getLastModificationDate().setTime(date.getTime());
    }

    private void setDeleteDate(Date date) {
        this.deleteDate = date;
    }

    public void moveToTrash() {
        this.deleted = true;
        setDeleteDate(new Date());
    }
}