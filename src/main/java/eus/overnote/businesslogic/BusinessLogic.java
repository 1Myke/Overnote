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
import java.util.Map;

public class BusinessLogic implements BlInterface {

    private final Logger logger = LoggerFactory.getLogger(BusinessLogic.class);
    private static BusinessLogic instance;
    private final DbAccessManager db;
    @Getter
    private OvernoteUser loggedInUser;
    /// A map that contains the {@link Note} class and its corresponding {@link NoteThumbnailController}.
    private final Map<Note, NoteThumbnailController> noteThumbnailControllerMap;
    @Setter
    @Getter
    /// The controller of the note editor component.
    private NoteController noteEditorController;
    @Getter
    /// The list of note thumbnail components that are displayed in the note list.
    private ObservableList<Node> thumbnails;

    private BusinessLogic() {
        db = new DbAccessManager();
        loggedInUser = db.getSession().getCurrentUser();
        thumbnails = FXCollections.observableArrayList();
        noteThumbnailControllerMap = new HashMap<>();
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

    /**
     * Sets the note that the user has currently selected.
     * The binding between the note editor and the thumbnail of the previous
     */
    @Override
    public void selectNote(Note note) {
        // Check if the user is logged in
        if (!isUserLoggedIn()) {
           throw new IllegalStateException("User is not logged in");
        }

        // Update the previous note in the database
        noteEditorController.saveNote();

        // Unbind the thumbnail of the previous selected note
        Note previousNote = loggedInUser.getSelectedNote();
        if (previousNote != null) {
            NoteThumbnailController thumbnailController = noteThumbnailControllerMap.get(previousNote);
            StringProperty thumbnailText = thumbnailController.getPreviewText().textProperty();
            StringProperty thumbnailTitle = thumbnailController.getTitleText().textProperty();

            thumbnailText.unbind();
            thumbnailTitle.unbind();
        }

        // Set the unselected style for all the thumbnails
        loggedInUser.setSelectedNote(note);
        noteThumbnailControllerMap.forEach((mappedNote, thumbnailController) ->
                thumbnailController.setSelectedStyle(false)
        );

        // Change the selected note
        noteEditorController.setSelectedNote(note);

        // Get the controller of the selected note
        NoteThumbnailController thumbnailController = noteThumbnailControllerMap.get(note);
        // Set the selected style for the selected thumbnail
        thumbnailController.setSelectedStyle(true);
        // Bind the thumbnail to the editor
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
        noteThumbnailControllerMap.get(note).updateContent();
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

    /**
     * Creates a new thumbnail for the note and adds it to the list of thumbnails.
     * @param note the note associated to the new thumbnail.
     */
    @Override
    public void addNewThumbnail(Note note) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NoteThumbnailController.class.getResource("note_thumbnail.fxml"));
            Node thumbnail = fxmlLoader.load();
            NoteThumbnailController controller = fxmlLoader.getController();
            controller.setNote(note);
            noteThumbnailControllerMap.put(note, controller);
            thumbnails.add(thumbnail);
        } catch (Exception e) {
            logger.error("Error loading note thumbnail", e);
        }

    }
}
