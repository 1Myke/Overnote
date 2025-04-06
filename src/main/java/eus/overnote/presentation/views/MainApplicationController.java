package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import eus.overnote.presentation.WindowManager;
import eus.overnote.presentation.components.NoteController;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(MainApplicationController.class);
    private final BlInterface bl = BusinessLogic.getInstance();
    private NoteController noteController;

    @FXML
    private VBox sidebarVBox;

    @FXML
    private Button newNoteAiButton;

    @FXML
    private BorderPane root;

    @FXML
    private Button newNoteButton;

    @FXML
    private MenuButton profileMenuButton;

    private ObservableList<Note> notes;
    private ObservableList<Node> thumbnails;

    public void initialize() {
        logger.debug("Initializing main application view");

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
        notes.forEach(this::addNewThumbnail);
    }

    void selectNote(Note note) {
        if (bl.getLoggedInUser() == null) {
            logger.error("No user logged in");
            return;
        }

        // Save the previous note
        Note previousNote = bl.getSelectedNote();
        if (previousNote != null) {
            logger.debug("Saving previous note");
            noteController.saveNote();
        } else {
            logger.debug("No previous note to save");
        }

        logger.debug("Selecting note");
        bl.selectNote(note);
        noteController.setSelectedNote(note);
    }

    @FXML
    void createNote(ActionEvent event) {
        if (bl.getLoggedInUser() == null) {
            logger.error("No user logged in");
            return;
        }

        logger.debug("Creating new note");
        Note createdNote = new Note("Untitled note", "", bl.getLoggedInUser());
        bl.saveNote(createdNote);
        notes.add(createdNote);
        addNewThumbnail(createdNote);
        selectNote(createdNote);
    }

    @FXML
    void onLogout(ActionEvent event) {
        logger.debug("Logging out user \"{}\"", bl.getLoggedInUser().getFullName());
        bl.logoutUser();
        WindowManager.getInstance().navigateToLogin();
    }

    private void addNewThumbnail(Note note) {
        bl.addNewThumbnail(note);
    }
}
