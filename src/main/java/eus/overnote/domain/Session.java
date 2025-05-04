package eus.overnote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn
    @Setter
    private OvernoteUser currentUser;

    // Singleton pattern to ensure only one session exists
    @Column(unique = true, nullable = false)
    private final boolean singleton = true;

    @Setter
    private boolean rememberMe = false;

    // Empty constructor for JPA
    public Session() {}

    public OvernoteUser getCurrentUser() {
        if (rememberMe) {
            return currentUser;
        } else {
            return null;
        }
    }
    public OvernoteUser getCurrentUserWithoutRememberMe() {
        return currentUser;
    }
}