package eus.overnote.factories;

import eus.overnote.presentation.components.CustomWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WindowFactory {
    private static final Logger logger = LoggerFactory.getLogger(WindowFactory.class);

    public static void createWindow(String title, Pane content, boolean resizable) throws IOException {
        logger.debug("Starting window creation for: title={}, content={}", title, content);

        FXMLLoader loader = new FXMLLoader(CustomWindowController.class.getResource("customwindow.fxml"));
        logger.debug("Custom window FXML loaded");
        Scene scene = new Scene(loader.load());
        scene.setFill(null);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        logger.debug("Showing window");

        CustomWindowController controller = loader.getController();
        controller.setTitle(title);
        controller.setContent(content);
        controller.setResizable(resizable);
        logger.debug("Setting up window controller");

        logger.info("Window created: title={}, content={}", title, content);
    }

    public static void createWindow(String title, Pane content) throws IOException {
        createWindow(title, content, false);
    }
}