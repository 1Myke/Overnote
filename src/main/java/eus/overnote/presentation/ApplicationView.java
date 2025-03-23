package eus.overnote.presentation;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.OvernoteUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ApplicationView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger("");
        logger.setLevel(Level.SEVERE);

        BlInterface bl = BusinessLogic.getInstance();
        bl.registerUser("Debug User", "debug@overnote.eus", "debug", "debug");
        OvernoteUser debugUser = bl.loginUser("debug@overnote.eus", "debug");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Overnote");
        stage.setScene(scene);
        stage.show();

        ApplicationViewController controller = fxmlLoader.getController();
        controller.setLoggedUser(debugUser);
    }


    public static void main(String[] args) {
        launch();
    }
}
