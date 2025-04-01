package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.businesslogic.RegisterException;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.WindowManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.*;

public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private BlInterface bl;

    @FXML
    private Label passwordMismatch;

    @FXML
    private Label errorInPassword;

    @FXML
    private Label errorInEmail;

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
        registerButton.setDisable(true);
        logger.debug("\"{}\" button listener initialized", registerButton.getText());
        signInButton.setOnAction(event -> navigateToLogin());
        logger.debug("\"{}\" button listener initialized", signInButton.getText());
        termsCheckbox.setOnAction(event -> { toggleCreation();});
        logger.debug("\"{}\" checkbox listener initialized", termsCheckbox.getText());
        // Initialize business logic
        bl = BusinessLogic.getInstance();
        logger.debug("Business logic initialized");
        errorInEmail.setVisible(false);
        errorInPassword.setVisible(false);
        passwordMismatch.setVisible(false);
    }

    private void toggleCreation() {
        registerButton.setDisable(!termsCheckbox.isSelected());

    }

    private void handleRegistration() {
        // Get form values
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();


        String confirmPassword = confirmPasswordField.getText();
        boolean termsAccepted = termsCheckbox.isSelected();

        logger.info("User tried to register. fullName=\"{}\", email=\"{}\", password=\"{}\", confirmPassword=\"{}\", termsAccepted=\"{}\"", fullName, email, password, confirmPassword, termsAccepted);



        // Call the business logic to register the user
        try{
        OvernoteUser user = bl.registerUser(fullName, email, password, confirmPassword);
        }
        catch (RegisterException e) {
            logger.error("Error registering user", e);
            if (!e.isEmailValid()) {
                errorInEmail.setVisible(true);
            } else {
                errorInEmail.setVisible(false);
            }
            if (!e.isPasswordValid()) {
                errorInPassword.setVisible(true);
            } else {
                errorInPassword.setVisible(false);
            }
            if (!e.isPasswordsMatch()) {
                passwordMismatch.setVisible(true);
            } else {
                passwordMismatch.setVisible(false);
            }

            return;
        }



        WindowManager.getInstance().navigateToMain();
    }

    private void navigateToLogin() {
        logger.info("User clicked on \"{}\"", signInButton.getText());
        WindowManager.getInstance().navigateToLogin();
    }

    public void clearFields() {
        // Clear the fields
        fullNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        termsCheckbox.setSelected(false);
    }
}