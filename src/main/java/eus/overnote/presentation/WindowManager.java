package eus.overnote.presentation;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.views.LoginController;
import eus.overnote.presentation.views.MainApplicationController;
import eus.overnote.presentation.views.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WindowManager {

    private final Logger logger = LoggerFactory.getLogger(WindowManager.class);


    // Singleton
    private static WindowManager INSTANCE;
    private WindowManager () {
        initialize();
    }
    public static WindowManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WindowManager();
        }
        return INSTANCE;
    }

    // Auth window
    private Stage authStage;
    private Scene loginScene;
    private Scene registerScene;
    // Main window
    private Stage mainStage;
    private Scene mainScene;
    private MainApplicationController mainController;
    private LoginController loginController;
    private RegisterController registerController;

    private void initialize() {
        // Creating the stages
        authStage = new Stage();
        authStage.setTitle("Overnote");
        mainStage = new Stage();
        mainStage.setTitle("Overnote");

        // Loading the scenes
        FXMLLoader loginLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        FXMLLoader registerLoader = new FXMLLoader(RegisterController.class.getResource("register.fxml"));
        try {
            loginScene = new Scene(loginLoader.load());
            loginController = loginLoader.getController();
        } catch (IOException e) {
            logger.error("Failed to load login scene", e);
            throw new RuntimeException(e);
        }
        try {
            registerScene = new Scene(registerLoader.load());
            registerController = registerLoader.getController();
        } catch (IOException e) {
            logger.error("Failed to load register scene", e);
            throw new RuntimeException(e);
        }
    }

    public void navigateToRegister() {
        mainStage.hide();
        authStage.setScene(registerScene);
        authStage.setWidth(1000);
        authStage.setHeight(750);
        authStage.show();
    }

    public void navigateToLogin() {
        mainStage.hide();
        authStage.setScene(loginScene);
        authStage.setWidth(1000);
        authStage.setHeight(750);
        authStage.show();
    }

    public void navigateToMain() {
        // Clear the fields of the credentials of the auth stage
        loginController.clearFields();
        registerController.clearFields();

        BlInterface bl = BusinessLogic.getInstance();
        if (!bl.isUserLoggedIn()) return;

        FXMLLoader mainLoader = new FXMLLoader(MainApplicationController.class.getResource("main.fxml"));
        try {
            mainScene = new Scene(mainLoader.load());
            mainController = mainLoader.getController();
        } catch (IOException e) {
            logger.error("Failed to load main scene", e);
            throw new RuntimeException(e);
        }

        mainStage.setScene(mainScene);
        mainStage.setWidth(1000);
        mainStage.setHeight(750);
        authStage.hide();
        mainStage.show();
    }
}
