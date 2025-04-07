package eus.overnote.presentation;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.presentation.components.NoteController;
import eus.overnote.presentation.views.LoginController;
import eus.overnote.presentation.views.MainApplicationController;
import eus.overnote.presentation.views.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WindowManager {

    private final Logger logger = LoggerFactory.getLogger(WindowManager.class);
    private BlInterface bl;



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
    // Components
    @Getter
    private Parent noteThumbnailParent;
    @Getter
    private Parent noteEditorParent;
    @Getter
    private NoteController noteEditorController;

    private void initialize() {
        bl = BusinessLogic.getInstance();

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

        // Load the note editor scene
        FXMLLoader fxmlLoader = new FXMLLoader(NoteController.class.getResource("note.fxml"));
        try {
            noteEditorParent = fxmlLoader.load();
            noteEditorController = fxmlLoader.getController();
        } catch (Exception e) {
            logger.error("Error loading note.fxml", e);
        }
        bl.setNoteEditorController(noteEditorController);
    }

    public void openApplication() {
        if (bl.isUserLoggedIn()) navigateToMain();
        else navigateToLogin();
    }

    public void navigateToRegister() {
        mainStage.hide();
        authStage.setScene(registerScene);
        authStage.show();
    }

    public void navigateToLogin() {
        mainStage.hide();
        authStage.setScene(loginScene);
        authStage.show();
    }

    /**
     * Clears the fields of the credentials of the auth stage,
     * checks if the user is logged in and loads the main scene.
     * It hides the auth stage and shows the main stage.
     */
    public void navigateToMain() {
        // Clear the fields of the credentials of the auth stage
        loginController.clearFields();
        registerController.clearFields();

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
        authStage.hide();
        mainStage.show();
    }
}
