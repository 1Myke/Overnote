package eus.overnote.presentation;

import eus.overnote.factories.WindowFactory;
import eus.overnote.presentation.views.LoginController;
import eus.overnote.presentation.views.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WindowManager extends Application {

    private static final Logger logger = LoggerFactory.getLogger(WindowManager.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            openLoginView();
        } catch (IOException e) {
            logger.error("Failed to open login screen: ", e);
        }
    }

    public static void openLoginView() throws IOException {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        Pane loginContent = loader.load();

        // Create custom window using the factory
        Stage loginWindow = WindowFactory.createWindow("Overnote - Login", loginContent);

        // Show the window
        loginWindow.show();

        logger.info("Login view opened");
    }

    public static void openRegisterView() throws IOException {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(RegisterController.class.getResource("register.fxml"));
        Pane registerContent = loader.load();

        // Create custom window using the factory
        Stage registerWindow = WindowFactory.createWindow("Overnote - Register", registerContent);

        // Show the window
        registerWindow.show();

        logger.info("Register view opened");
    }
}
