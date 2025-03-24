package eus.overnote.presentation;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.views.MainApplicationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        BlInterface bl = BusinessLogic.getInstance();
        bl.registerUser("Debug User", "debug@overnote.eus", "debug", "debug");
        OvernoteUser debugUser = bl.loginUser("debug@overnote.eus", "debug");

        WindowManager.getInstance().navigateToMain(debugUser);
    }


    public static void main(String[] args) {
        launch();
    }
}
