package eus.overnote.factories;

import eus.overnote.presentation.LoginController;
import eus.overnote.presentation.components.TitleBarLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class WindowFactory {

    public static Stage createWindow(String title, Pane content) throws IOException {
        // Set up the window
        BorderPane root = new BorderPane();
        root.setTop(TitleBarLoader.load(title));
        root.setCenter(content);

        // Set up window clipping
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(root.widthProperty());
        clip.heightProperty().bind(root.heightProperty());
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        root.setClip(clip);

        // Configure the scene
        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        scene.getStylesheets().add(LoginController.class.getResource("styles.css").toExternalForm());

        // Set the stage
        Stage stage = new Stage();
        stage.setScene(scene);

        // Remove default window decoration
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);

        // Set the application icon
        // stage.getIcons().add(MainApplication.APP_ICON);
        return stage;
    }
}