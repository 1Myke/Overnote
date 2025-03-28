package eus.overnote.businesslogic;

import eus.overnote.data_access.DbAccessManager;
import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import lombok.Getter;

public class BusinessLogic implements BlInterface {

    private static BusinessLogic instance;
    private final DbAccessManager db;
    @Getter
    private OvernoteUser loggedInUser;

    private BusinessLogic() {
        db = new DbAccessManager();
    }

    public static BusinessLogic getInstance() {
        if (instance == null) {
            instance = new BusinessLogic();
        }
        return instance;
    }

    @Override
    public OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return null;
        }

        OvernoteUser user = db.registerUser(fullName, email, password);
        loggedInUser = user;
        return user;
    }

    @Override
    public OvernoteUser loginUser(String email, String password) {
        OvernoteUser user = db.loginUser(email, password);
        loggedInUser = user;
        return user;
    }

    @Override
    public void logoutUser() {
        loggedInUser = null;
    }

    @Override
    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    @Override
    public void saveNote(Note note) {
        db.saveNote(note);
    }

    @Override
    public void updateNote(Note note) {
        db.updateNote(note);
    }

    @Override
    public void deleteNote(Note note) {
        db.deleteNote(note);
    }
}
