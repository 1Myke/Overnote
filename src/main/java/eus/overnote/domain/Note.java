package eus.overnote.domain;

import eus.overnote.presentation.components.NoteEditorController;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;

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

    @Lob
    @Column(nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    private final Date creationDate = new Date();

    @Column(nullable = false)
    private Date lastModificationDate = new Date();

    private Date deleteDate;

    @ManyToOne()
    @Setter
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

    // Complete constructor the Id is generated here to resolve bugs
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

    public void recover() {
        this.deleted = false;
        this.deleteDate = null;
    }

    public boolean matchesContent(String content) {

        // Compare raw title, compare parsed content
        return this.title.toLowerCase().contains(content.toLowerCase()) ||
                Jsoup.parse(this.content).text().toLowerCase().contains(content.toLowerCase());
    }
}