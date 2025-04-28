package eus.overnote.presentation.components;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class NoteEditorController {

    private static final Logger logger = LoggerFactory.getLogger(NoteEditorController.class);
    private Note selectedNote;
    /// The timer to detect user inactivity.
    private final PauseTransition savePause = new PauseTransition(javafx.util.Duration.seconds(3));
    BlInterface bl = BusinessLogic.getInstance();

    @FXML
    private StackPane root;

    @FXML
    @Getter
    private TextArea noteText;

    @FXML
    @Getter
    private TextField noteTitle;

    public void setSelectedNote(Note note) {
        selectedNote = note;
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getContent());
        savePause.stop();
        root.setVisible(true);
    }

    public void initialize() {
        root.setVisible(false);
        // Set a timer to save the note after the user idles
        savePause.setOnFinished(event -> updateNote());
        noteText.textProperty().addListener((observable, oldValue, newValue) -> savePause.playFromStart());
        noteTitle.textProperty().addListener((observable, oldValue, newValue) -> savePause.playFromStart());
    }

    /**
     * This method is called when the user clicks on the save button.
     * It updates the attributes of the selected note with the values of the text fields
     * and calls the business logic to update the note in the database.
     */
    public void updateNote() {
        if (selectedNote != null){
            selectedNote.setTitle(noteTitle.getText());
            selectedNote.setContent(noteText.getText());
            selectedNote.setLastModificationDate(new Date());
            bl.updateNote(selectedNote);
            logger.debug("Note {} saved for user {}", selectedNote.getId(), selectedNote.getUser().getEmail());
            logger.debug("{}'s notes: {}", selectedNote.getUser().getEmail(), selectedNote.getUser().getNotes());
        }
    }

    @FXML
    public void moveToTrash() {
        if (selectedNote != null) {
            logger.debug("Note {} moved to trash for user {}", selectedNote.getId(), selectedNote.getUser().getEmail());
            bl.moveNoteToTrash(selectedNote);
        }
    }

    public void clearEditor() {
        root.setVisible(false);
        noteText.clear();
        noteTitle.clear();
        savePause.stop();
    }
}
