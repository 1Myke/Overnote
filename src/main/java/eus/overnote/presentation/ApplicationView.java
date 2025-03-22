package eus.overnote.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static javafx.application.Application.launch;

public class ApplicationView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger("");
        logger.setLevel(Level.SEVERE);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Overnote");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
