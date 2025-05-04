package eus.overnote.businesslogic;

import eus.overnote.data_access.DbAccessManager;
import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.domain.Session;
import eus.overnote.presentation.WindowManager;
import eus.overnote.presentation.components.NoteEditorController;
import eus.overnote.presentation.components.NoteThumbnailController;
import eus.overnote.presentation.views.MainApplicationController;
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
import java.util.UUID;
import java.io.*;
import java.util.*;

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
    private NoteEditorController noteEditorController;
    @Getter
    /// The list of note thumbnail components that are displayed in the note list.
    private ObservableList<Node> thumbnails;
    @Getter
    /// The controller of the main application window.
    private MainApplicationController mainApplicationController;

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
    public boolean validateEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    @Override
    public boolean validatePassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
    }
    @Override
    public boolean validatePasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
    @Override
    public List<Note> getNotesFromUserId() {
        return db.getNotesbyUserID();
    }

    @Override
    public OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword) throws RegisterException {
        boolean validEmail = true;
        boolean validPassword = true;
        boolean passwordsMatch  = validatePasswordMatch(password, confirmPassword);

        if (!validateEmail(email)) {
            validEmail = false;
        }

        if (!validatePassword(password)) {
            validPassword = false;
        }
        if (!validEmail || !validPassword || !passwordsMatch) {
            throw new RegisterException("Invalid registration data", validEmail, validPassword, passwordsMatch);
        }

        String hashedPassword = hashPassword(password);
        OvernoteUser user = db.registerUser(fullName, email, hashedPassword);
        setLoggedInUser(user);
        logger.info("Session created for user {}", user.getEmail());
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
            checkNotesForDeletion();
            return user;
        } else
            return null;
    }

    /**
     * Delete the notes that are marked as trashed
     * if they were deleted more than 30 days ago.
     */
    private void checkNotesForDeletion() {
        for (Note note : loggedInUser.getNotes()) {
            if (note.isDeleted()) {
                long diff = System.currentTimeMillis() - note.getDeleteDate().getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                if (days > 30) {
                    db.deleteNote(note);
                }
            }
        }
    }

    @Override
    public void logoutUser() {
        for (Note note : loggedInUser.getNotes()) {
            noteThumbnailControllerMap.remove(note);
            logger.debug("Removing thumbnail for note {}", note.getId());
        }


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
        if(noteEditorController.getSelectedNoteNote() != null) {
            noteEditorController.updateNote();
        }

        // Unbind the thumbnail of the previous selected note
        Note previousNote = loggedInUser.getSelectedNote();
        if (previousNote != null) {
            NoteThumbnailController thumbnailController = noteThumbnailControllerMap.get(previousNote);
            StringProperty thumbnailTitle = thumbnailController.getTitleText().textProperty();
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
        StringProperty thumbnailTitle = thumbnailController.getTitleText().textProperty();
        StringProperty editorTitle = noteEditorController.getNoteTitle().textProperty();
        noteEditorController.bindThumbnailController(thumbnailController);

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
    public void moveNoteToTrash(Note note) {
        noteEditorController.updateNote();
        removeThumbnail(note);
        db.moveNoteToTrash(note);
        noteEditorController.clearEditor();
    }

    @Override
    public void deleteNote(Note note) {
        db.deleteNote(note);
    }

    private void setLoggedInUser(OvernoteUser user) {
        if (!(user == null)) {

            logger.error("Setting logged in user to {}", user.getEmail());
        }
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
        if (noteThumbnailControllerMap.containsKey(note))
        {
            logger.debug("Thumbnail already exists for note: {}", note.getTitle());
            noteThumbnailControllerMap.get(note).show();
        } else {
            try {
                //here the id is right

                logger.info("this note's id is \"{}\"",note.getId());
                FXMLLoader fxmlLoader = new FXMLLoader(NoteThumbnailController.class.getResource("note_thumbnail.fxml"));
                Node thumbnail = fxmlLoader.load();
                NoteThumbnailController controller = fxmlLoader.getController();
                controller.setNote(note);
                //here the id is right

                noteThumbnailControllerMap.put(note, controller);
                thumbnails.add(0,thumbnail);

                // Don't show the thumbnail if the note is deleted
                if (note.isDeleted()) controller.hide();
            } catch (Exception e) {
                logger.error("Error loading note thumbnail", e);
            }
        }
    }

    private void removeThumbnail(Note note) {
        noteThumbnailControllerMap.get(note).hide();

        // Unbind the thumbnail from the editor
        StringProperty thumbnailTitle = noteThumbnailControllerMap.get(note).getTitleText().textProperty();
        thumbnailTitle.unbind();
    }

    @Override
    public NoteThumbnailController getThumbnailController(Note note) {
        return noteThumbnailControllerMap.get(note);
    }

    //
    // ALL THE RELATED THING WITH THE LANGUAGES AND CONFIGURATION.PROPERTIES
    //

    private static final String CONFIG_FILE = "configuration.properties";
    private static final String LANGUAGE_KEY = "language";

    @Override
    public void changeLanguage(Locale locale) {
        if (locale != null) {
            Locale.setDefault(locale);
            logger.info("Language changed to: {}", locale);

            Properties properties = new Properties();
            try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
                properties.setProperty(LANGUAGE_KEY, locale.toString());
                properties.store(output, "User Language Configuration");
            } catch (IOException e) {
                logger.error("Error saving language configuration", e);
            }
        } else {
            logger.error("Locale is null");
        }

        thumbnails.clear();
        noteThumbnailControllerMap.clear();
        WindowManager.getInstance().reloadAllScenes();

    }

    @Override
    public Locale loadLanguage() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            String language = properties.getProperty(LANGUAGE_KEY, "en");
            Locale locale = Locale.forLanguageTag(language);
            // Set the default locale for the application
            Locale.setDefault(locale);
            logger.info("Language loaded: {}", locale);
            return locale;
        } catch (IOException e) {
            logger.error("Error loading user Language Configuration", e);
        }
        // If an error occurs, return the default language of the computer
        return Locale.getDefault();
    }

    @Override
    public String getTranslation(String s) {
        ResourceBundle rb = ResourceBundle.getBundle("eus.overnote.presentation.messages", Locale.getDefault());
        try {
            return rb.getString(s);
        } catch (MissingResourceException e) {
            logger.error("Error loading translation for key: {}", s, e);
            return s;
        }
    }
}
