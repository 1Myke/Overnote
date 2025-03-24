package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.presentation.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private BlInterface bl;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox termsCheckbox;

    @FXML
    private Button registerButton;

    @FXML
    private Button signInButton;

    @FXML
    public void initialize() {
        // Set up event handlers for registration functionality
        registerButton.setOnAction(event -> handleRegistration());
        logger.debug("\"{}\" button listener initialized", registerButton.getText());
        signInButton.setOnAction(event -> navigateToLogin());
        logger.debug("\"{}\" button listener initialized", signInButton.getText());

        // Initialize business logic
        bl = new BusinessLogic();
        logger.debug("Business logic initialized");
    }

    private void handleRegistration() {
        // Get form values
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        boolean termsAccepted = termsCheckbox.isSelected();

        logger.info("User tried to register. fullName=\"{}\", email=\"{}\", password=\"{}\", confirmPassword=\"{}\", termsAccepted=\"{}\"", fullName, email, password, confirmPassword, termsAccepted);

        // Check if the terms have been accepted (likely to be removed)
        if (!termsAccepted) {
            logger.info("User did not accept the terms and conditions. Registration aborted.");
            return;
        }

        // Call the business logic to register the user
        bl.registerUser(fullName, email, password, confirmPassword);
    }

    private void navigateToLogin() {
        logger.info("User clicked on \"{}\"", signInButton.getText());
        WindowManager.navigateToLogin();
    }
}