package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import eus.overnote.presentation.WindowManager;
import eus.overnote.presentation.components.NoteEditorController;
import eus.overnote.presentation.components.NoteThumbnailController;
import eus.overnote.presentation.components.ProfileBannerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Locale;

public class MainApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(MainApplicationController.class);
    private final BlInterface bl = BusinessLogic.getInstance();
    private NoteEditorController noteEditorController;

    @FXML
    private VBox sidebarVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button newNoteAiButton;

    @FXML
    private BorderPane root;

    @FXML
    private Button newNoteButton;

    @FXML
    private MenuButton profileMenuButton;

    @FXML
    private MenuItem en;

    @FXML
    private MenuItem es;

    @FXML
    private MenuItem eu;

    private ObservableList<Note> notes;
    private ObservableList<Node> thumbnails;

    public void initialize() {
        logger.debug("Initializing main application view");

        noteEditorController = bl.getNoteEditorController();

        // Initialize note list
        notes = FXCollections.observableArrayList(bl.getLoggedInUser().getNotes());

        // Load the profile banner FXML
        try {
            profileMenuButton.setGraphic(FXMLLoader.load(ProfileBannerController.class.getResource("profilebanner.fxml")));
            logger.debug("Profile banner loaded");
        } catch (Exception e) {
            profileMenuButton.setGraphic(new Text("Error loading profile banner"));
            logger.error("Error loading profile banner", e);
        }

        // Load Note FXML
        root.setCenter(WindowManager.getInstance().getNoteEditorParent());

        // Initialize the sidebar
        thumbnails = bl.getThumbnails();
        Bindings.bindContent(sidebarVBox.getChildren(), thumbnails);
        notes.forEach(bl::addNewThumbnail);

        // Bind the searchbar with the visible thumbnails
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            notes.forEach(note -> {
                NoteThumbnailController thumbnail = bl.getThumbnailController(note);
                if (note.matchesContent(newValue)) {
                    thumbnail.show();
                } else {
                    thumbnail.hide();
                }
            });
        });

        // Language menu listeners
        en.setOnAction(event -> bl.changeLanguage(Locale.ENGLISH));
        es.setOnAction(event -> bl.changeLanguage(new Locale("es")));
        eu.setOnAction(event -> bl.changeLanguage(new Locale("eu")));
    }

    /**
     * Calls the controller of the note editor to save the noted that
     * was previously selected. Calls the business logic to select the
     * new note.
     *
     * @param note the note to select.
     */
    private void selectNote(Note note) {
        if (bl.getLoggedInUser() == null) {
            logger.error("No user logged in");
            return;
        }

        // Save the previous note
        logger.info("Saving the current note before selecting the next.");
        noteEditorController.saveNote();

        logger.debug("Selecting note");
        bl.selectNote(note);
    }

    /**
     * This method is called when the button for creating notes is pressed.
     * Creates a new instance of {@link Note} and calls the business logic to
     * save it in the database. It automatically selects the created note.
     *
     * @param event the {@link ActionEvent} created on the interaction.
     */
    @FXML
    void createNote(ActionEvent event) {
        if (bl.getLoggedInUser() == null) {
            logger.error("No user logged in");
            return;
        }

        logger.debug("Creating new note");
        Note createdNote = new Note(bl.getTranslation("note.default.title"), "", bl.getLoggedInUser());
        bl.saveNote(createdNote);
        notes.add(createdNote);
        bl.addNewThumbnail(createdNote);
        selectNote(createdNote);
    }

    /**
     * This method is called when the log out option is selected from the
     * profile menu. It calls the business logic to log out the user and
     * navigates to the login view.
     *
     * @param event the {@link ActionEvent} created on the interaction.
     */
    @FXML
    void onLogout(ActionEvent event) {
        logger.debug("Logging out user \"{}\"", bl.getLoggedInUser().getFullName());
        bl.logoutUser();
        WindowManager.getInstance().navigateToLogin();
    }

    @FXML
    void focusSearchBar(MouseEvent event) {
        searchTextField.requestFocus();
    }
}
