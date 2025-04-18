package eus.overnote.presentation.components;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.Getter;

public class NoteThumbnailController {

    private Note note;
    private final BlInterface bl = BusinessLogic.getInstance();

    @FXML
    @Getter
    private StackPane root;

    @FXML
    private Text dateText;

    @FXML
    @Getter
    private Label previewTextLabel;

    @FXML
    private FlowPane tagFlowPane;

    @FXML
    @Getter
    private Text titleText;

    public void initialize() {
    }

    /**
     * Sets the {@link NoteThumbnailController#note} attribute of this
     * {@link NoteThumbnailController}.
     * Sets the content of the thumbnail to match the attributes of the {@code note}.
     * @param note The note to set.
     */
    public void setNote(Note note) {
        this.note = note;
        // Values updated via bindings
        titleText.setText(note.getTitle());
        previewTextLabel.setText(note.getContent());
        // Manual update
        updateContent();
    }

    /**
     * This method is called when the user clicks on the note thumbnail.
     * It calls the business logic to select the note associated to the
     * clicked thumbnail.
     */
    @FXML
    private void selectNote() {
        bl.selectNote(this.note);
    }

    /**
     * Sets if the thumbnail has been selected to update the UI accordingly.
     * @param selected whether the thumbnail will be selected or not.
     */
    public void setSelectedStyle(boolean selected) {
        if (selected) {
            root.getStyleClass().setAll("note-thumbnail-selected");
        } else {
            root.getStyleClass().setAll("note-thumbnail");
        }
    }

    public void updateContent() {
        dateText.setText(note.getLastModificationDate().toString());
        tagFlowPane.getChildren().clear();
        note.getTags().forEach(tag -> {
            Text tagText = new Text(tag.toString());
            tagFlowPane.getChildren().add(tagText);
        });
    }

    public void hide() {
        root.setManaged(false);
        root.setVisible(false);
    }

    public void show() {
        root.setManaged(true);
        root.setVisible(true);
    }
}