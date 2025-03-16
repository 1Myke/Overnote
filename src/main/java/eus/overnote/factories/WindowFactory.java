package eus.overnote.factories;

import eus.overnote.presentation.components.CustomWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class WindowFactory {

    public static void createWindow(String title, Pane content, boolean resizable) throws IOException {
        FXMLLoader loader = new FXMLLoader(CustomWindowController.class.getResource("customwindow.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setFill(null);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.show();

        CustomWindowController controller = loader.getController();
        controller.setTitle(title);
        controller.setContent(content);
        controller.setResizable(resizable);
    }

    public static void createWindow(String title, Pane content) throws IOException {
        createWindow(title, content, false);
    }
}