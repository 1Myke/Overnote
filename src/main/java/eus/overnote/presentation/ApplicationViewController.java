package eus.overnote.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationViewController {

    @FXML
    private Button aiButton;

    @FXML
    private BorderPane appBorderPane;

    @FXML
    private Button newNodeButton;

    @FXML
    void createNote(ActionEvent event) {
        loadContent("note.fxml");
    }

    //Juanan's method to add content in a border pain (ADAPTED)

    private final Map<String, AnchorPane> contentCache = new HashMap<>();

    private void loadContent(String fxmlFile) {
        try {
            // Check if content is already cached
            AnchorPane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();
                contentCache.put(fxmlFile, content);
            }
            appBorderPane.setRight(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
