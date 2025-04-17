package eus.overnote.businesslogic;

import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;

public interface BlInterface {
    // User
    OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword);
    OvernoteUser loginUser(String email, String password, boolean rememberMe);
    OvernoteUser getLoggedInUser();

    void logoutUser();

    boolean isUserLoggedIn();

    String hashPassword(String password);

    boolean checkPassword(String password, String hashedPassword);
    boolean validateEmail(String email);
    boolean validatePassword(String password);
    boolean validatePasswordMatch(String password, String confirmPassword);
    // Note
    void saveNote(Note note);
    void updateNote(Note note);
    void moveToDeleteNote(Note note);
    void deleteNote(Note note);
}