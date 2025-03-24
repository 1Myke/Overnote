package eus.overnote.presentation;

import eus.overnote.presentation.views.LoginController;
import eus.overnote.presentation.views.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WindowManager extends Application {

    private static final Logger logger = LoggerFactory.getLogger(WindowManager.class);
    // Auth window
    private static Stage authStage;
    private static Scene loginScene;
    private static Scene registerScene;
    // Main window
    private static Stage mainStage;
    private static Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Overnote application");
        initialize();

        // The auth screen should be the first one to show
        authStage.setScene(loginScene);
        authStage.show();
    }

    private void initialize() {
        // Creating the stages
        authStage = new Stage();
        authStage.setTitle("Overnote");
        mainStage = new Stage();
        mainStage.setTitle("Overnote");

        // Loading the scenes
        FXMLLoader loginLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        FXMLLoader registerLoader = new FXMLLoader(RegisterController.class.getResource("register.fxml"));
        FXMLLoader mainLoader = null; // TODO
        try {
            loginScene = new Scene(loginLoader.load());
        } catch (IOException e) {
            logger.error("Failed to load login scene", e);
            throw new RuntimeException(e);
        }
        try {
            registerScene = new Scene(registerLoader.load());
        } catch (IOException e) {
            logger.error("Failed to load register scene", e);
            throw new RuntimeException(e);
        }
        // TODO: Add the main window initialization after merging
    }

    public static void navigateToRegister() {
        authStage.setScene(registerScene);
    }

    public static void navigateToLogin() {
        authStage.setScene(loginScene);
    }
}
