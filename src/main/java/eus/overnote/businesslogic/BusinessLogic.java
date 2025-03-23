package eus.overnote.businesslogic;

import eus.overnote.data_access.DbAccessManager;
import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;

public class BusinessLogic implements BlInterface {

    private static BusinessLogic instance;
    private final DbAccessManager db;

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

        return db.registerUser(fullName, email, password);
    }

    @Override
    public OvernoteUser loginUser(String email, String password) {
        return db.loginUser(email, password);
    }

    // Notes

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
