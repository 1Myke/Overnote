package eus.overnote.businesslogic;

import eus.overnote.data_access.DbAccessManager;
import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.domain.Session;
import eus.overnote.presentation.components.NoteController;
import eus.overnote.presentation.components.NoteThumbnailController;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessLogic implements BlInterface {

    private final Logger logger = LoggerFactory.getLogger(BusinessLogic.class);
    private static BusinessLogic instance;
    private final DbAccessManager db;
    @Getter
    private OvernoteUser loggedInUser;
    private Map<Note, NoteThumbnailController> noteControllerMap;
    @Setter
    private NoteController noteEditorController;
    @Getter
    private ObservableList<Node> thumbnails;

    private BusinessLogic() {
        db = new DbAccessManager();
        loggedInUser = db.getSession().getCurrentUser();
        noteControllerMap = new HashMap<>();
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

        String hashedPassword = hashPassword(password);
        OvernoteUser user = db.registerUser(fullName, email, hashedPassword);
        setLoggedInUser(user);
        return user;
    }

    @Override
    public OvernoteUser loginUser(String email, String password, boolean rememberMe) {
        OvernoteUser user = db.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (checkPassword(password, user.getPassword())) {
            setLoggedInUser(user, rememberMe);
            thumbnails = FXCollections.observableArrayList();
            return user;
        } else
            return null;
    }

    @Override
    public void logoutUser() {
        setLoggedInUser(null);
        Session session = db.getSession();
        session.setRememberMe(false);
        db.saveSession(session);
    }

    @Override
    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public void selectNote(Note note) {
        if (!isUserLoggedIn()) {
           throw new IllegalStateException("User is not logged in");
        }

        // Unbind the thumbnail to the editor
        loggedInUser.setSelectedNote(note);
        noteControllerMap.forEach((mappedNote,thumbnailController) -> {
            thumbnailController.setSelectedStyle(false);
            StringProperty thumbnailText = thumbnailController.getPreviewText().textProperty();
            StringProperty thumbnailTitle = thumbnailController.getTitleText().textProperty();

            thumbnailText.unbind();
            thumbnailTitle.unbind();
        });

        // Change the selected note
        noteEditorController.setSelectedNote(note);

        // Bind the thumbnail to the editor
        NoteThumbnailController thumbnailController = noteControllerMap.get(note);
        thumbnailController.setSelectedStyle(true);

        StringProperty thumbnailText = thumbnailController.getPreviewText().textProperty();
        StringProperty thumbnailTitle = thumbnailController.getTitleText().textProperty();
        StringProperty editorText = noteEditorController.getNoteText().textProperty();
        StringProperty editorTitle = noteEditorController.getNoteTitle().textProperty();

        thumbnailText.bind(editorText);
        thumbnailTitle.bind(editorTitle);
    }

    @Override
    public Note getSelectedNote() {
        if (isUserLoggedIn()) {
            return loggedInUser.getSelectedNote();
        }
        return null;
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

    private void setLoggedInUser(OvernoteUser user) {
        this.loggedInUser = user;
        Session session = db.getSession();
        session.setCurrentUser(user);
        db.saveSession(session);
    }

    private void setLoggedInUser(OvernoteUser user, boolean rememberMe) {
        setLoggedInUser(user);
        Session session = db.getSession();
        session.setRememberMe(rememberMe);
        db.saveSession(session);
    }

    @Override
    public List<Note> getNotes() {
        return loggedInUser.getNotes();
    }

    @Override
    public void addNewThumbnail(Note note) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NoteThumbnailController.class.getResource("note_thumbnail.fxml"));
            Node thumbnail = fxmlLoader.load();
            NoteThumbnailController controller = fxmlLoader.getController();
            controller.setNote(note);
            noteControllerMap.put(note, controller);
            thumbnails.add(thumbnail);
        } catch (Exception e) {
            logger.error("Error loading note thumbnail", e);
        }

    }
}
