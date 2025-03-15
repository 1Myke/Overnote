package eus.overnote.presentation;

import eus.overnote.factories.WindowFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginApplication extends Application {

    public void start(Stage primaryStage) throws Exception {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("login.fxml"));
        Pane loginContent = loader.load();

        // Create custom window using the factory
        Stage loginWindow = WindowFactory.createWindow("Overnote - Login", loginContent);

        // Show the window
        loginWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}