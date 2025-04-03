package eus.overnote.presentation.components;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private Note selectedNote;
    @Setter
    private NoteThumbnailController thumbnailController;
    private final PauseTransition savePause = new PauseTransition(javafx.util.Duration.seconds(3));
    BlInterface bl = BusinessLogic.getInstance();

    @FXML
    private TextArea noteText;

    @FXML
    private TextField noteTitle;


    public void setSelectedNote(Note note) {
        selectedNote = note;
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getContent());
    }

    public void initialize() {
        savePause.setOnFinished(event -> saveNote());
        noteText.textProperty().addListener((observable, oldValue, newValue) -> savePause.playFromStart());
        noteTitle.textProperty().addListener((observable, oldValue, newValue) -> savePause.playFromStart());
    }

    public void saveNote() {
        if (selectedNote != null){
            selectedNote.setTitle(noteTitle.getText());
            selectedNote.setContent(noteText.getText());
            selectedNote.setLastModificationDate(new Date());
            bl.updateNote(selectedNote);
            logger.debug("Note {} saved for user {}", selectedNote.getId(), selectedNote.getUser().getEmail());
            logger.debug("{}'s notes: {}", selectedNote.getUser().getEmail(), selectedNote.getUser().getNotes());
        }
    }
}
