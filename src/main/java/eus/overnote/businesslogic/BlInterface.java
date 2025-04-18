package eus.overnote.businesslogic;

import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.components.NoteController;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public interface BlInterface {
    // User
    OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword);
    OvernoteUser loginUser(String email, String password, boolean rememberMe);
    OvernoteUser getLoggedInUser();
    void logoutUser();
    boolean isUserLoggedIn();

    // Security
    String hashPassword(String password);
    boolean checkPassword(String password, String hashedPassword);
    boolean validateEmail(String email);
    boolean validatePassword(String password);
    boolean validatePasswordMatch(String password, String confirmPassword);

    // Note
    void selectNote(Note note);
    void saveNote(Note note);
    void updateNote(Note note);
    void setNoteEditorController(NoteController noteEditorController);
    NoteController getNoteEditorController();
    void addNewThumbnail(Note note);
    ObservableList<Node> getThumbnails();
    Note getSelectedNote();
    void moveNoteToTrash(Note note);
    void deleteNote(Note note);
}