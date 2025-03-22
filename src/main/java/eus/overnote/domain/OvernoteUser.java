package eus.overnote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class OvernoteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Setter
    private String fullName;

    @Column(unique = true, nullable = false)
    @Setter
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

    @Column(nullable = false, updatable = false)
    private Date registrationDate = new Date();

    /*
Mikel Martinez: I have commented this part because it didn't work correctly, we need to correct it
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();*/

    // Empty constructor for JPA
    protected OvernoteUser() {
    }

    // Complete constructor
    public OvernoteUser(String fullName, String email, String password) {
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.registrationDate = new Date();
    }
}