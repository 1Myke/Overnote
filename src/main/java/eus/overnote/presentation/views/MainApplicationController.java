package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.components.NoteController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(MainApplicationController.class);
    private final BlInterface bl = BusinessLogic.getInstance();
    private OvernoteUser loggedUser;
    private NoteController noteController;

    @FXML
    private Button newNoteAiButton;

    @FXML
    private BorderPane root;

    @FXML
    private Button newNoteButton;

    @FXML
    private ComboBox<Note> noteSelectComboBox;
    private ObservableList<Note> notes;

    public void initialize() {
        logger.debug("Initializing main application view");

        // Set selection comboBox
        notes = FXCollections.observableArrayList();
        noteSelectComboBox.setItems(notes);

        // Load Note FXML
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Note.fxml"));
            root.setCenter(fxmlLoader.load());
            noteController = fxmlLoader.getController();
        } catch (Exception e) {
            logger.error("Error loading Note.fxml", e);
        }
    }

    void selectNote(Note note) {
        if (loggedUser == null) {
            logger.error("No user logged in");
            return;
        }
        logger.debug("Selecting note");
        noteController.setSelectedNote(note);
    }

    @FXML
    void createNote(ActionEvent event) {
        if (loggedUser == null) {
            logger.error("No user logged in");
            return;
        }

        logger.debug("Creating new note");
        Note createdNote = new Note("Untitled note", "", loggedUser);
        bl.saveNote(createdNote);
        notes.add(createdNote);
        noteSelectComboBox.getSelectionModel().select(createdNote);
        selectNote(createdNote);
    }

    public void setLoggedUser(OvernoteUser user) {
        loggedUser = user;
        notes.addAll(user.getNotes());
        noteSelectComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            noteController.saveNote();
            selectNote(newValue);
        });
    }
}
