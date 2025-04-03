package eus.overnote.presentation.components;

import eus.overnote.domain.Note;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class NoteThumbnailController {

    private Note note;

    @FXML
    private Text dateText;

    @FXML
    private Text previewText;

    @FXML
    private FlowPane tagFlowPane;

    @FXML
    private Text titleText;

    public void updateThumbnail() {
        if (note != null) {
            titleText.setText(note.getTitle());
            previewText.setText(note.getContent());
            dateText.setText(note.getLastModificationDate().toString());
            tagFlowPane.getChildren().clear();
            note.getTags().forEach(tag -> {
                Text tagText = new Text(tag.toString());
                tagFlowPane.getChildren().add(tagText);
            });
        }
    }

    public void setNote(Note note) {
        this.note = note;

        titleText.setText(note.getTitle());
        previewText.setText(note.getContent());
        dateText.setText(note.getLastModificationDate().toString());
        tagFlowPane.getChildren().clear();
        note.getTags().forEach(tag -> {
            Text tagText = new Text(tag.toString());
            tagFlowPane.getChildren().add(tagText);
        });
    }
}