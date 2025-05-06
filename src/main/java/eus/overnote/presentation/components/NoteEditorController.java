package eus.overnote.presentation.components;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class NoteEditorController {

    private static final Logger logger = LoggerFactory.getLogger(NoteEditorController.class);
    private Note selectedNote;
    private NoteThumbnailController bindedThumbnailController;

    /// The timer to detect user inactivity.
    private final PauseTransition savePause = new PauseTransition(javafx.util.Duration.seconds(0.25));
    BlInterface bl = BusinessLogic.getInstance();

    @FXML
    private StackPane root;

    @FXML
    @Getter
    private TextField noteTitle;

    @FXML
    private HTMLEditor htmlEditor;

    public void setSelectedNote(Note note) {
        // here the id is right
        logger.info(" Setting selected note to {}", note.getId());
        selectedNote = note;
        noteTitle.setText(note.getTitle());
        htmlEditor.setHtmlText(note.getContent());
        savePause.stop();
        root.setVisible(true);
    }

    public void bindThumbnailController(NoteThumbnailController thumbnailController) {
        bindedThumbnailController = thumbnailController;
    }

    public void initialize() {
        root.setVisible(false);
        // Set a timer to save the note after the user idles
        savePause.setOnFinished(event -> {updateNote();selectedNote.setLastModificationDate(new Date());});
        htmlEditor.setOnKeyReleased(event -> onNoteUpdate());
        htmlEditor.setOnMouseClicked(event -> onNoteUpdate());
        noteTitle.textProperty().addListener((observable, oldValue, newValue) -> savePause.playFromStart());

        //Add a key event handler for saving notes with the combination "Ctrl + S"
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown() && event.getCode() == javafx.scene.input.KeyCode.S) {
                        updateNote();
                        selectedNote.setLastModificationDate(new Date());
                        event.consume(); // Prevent further handling of the event
                    }
                });
            }
        });
    }

    /**
     * This method is called when the user clicks on the save button.
     * It updates the attributes of the selected note with the values of the text fields
     * and calls the business logic to update the note in the database.
     */


    public void updateNote() {
        if (selectedNote != null){
            logger.debug("Saving note {} for user {}", selectedNote.getId(), selectedNote.getUser().getEmail());

            String titleText = noteTitle.getText();
            String htmlText = htmlEditor.getHtmlText();

            // Check there have been changes
            if (htmlText.equals(selectedNote.getContent()) && titleText.equals(selectedNote.getTitle())) {
                logger.debug("No changes detected for note {}", selectedNote.getId());
                return;
            }

            selectedNote.setTitle(titleText);
            selectedNote.setContent(htmlText);
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

    @FXML
    void saveNoteClickingButton() {
        updateNote();
    }

    public void clearEditor() {
        root.setVisible(false);
        htmlEditor.setHtmlText("");
        noteTitle.clear();
        savePause.stop();
    }

    public Note getSelectedNoteNote() {
        return  this.selectedNote;
    }

    private void onNoteUpdate() {
        // Update thumbnail webview
        bindedThumbnailController.setPreviewText(Jsoup.parse(htmlEditor.getHtmlText()).text());
        savePause.playFromStart();
    }


}
