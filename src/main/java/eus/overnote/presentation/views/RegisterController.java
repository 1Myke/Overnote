package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.*;

public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private BlInterface bl;

    @FXML
    private Label ErrorInPassword;

    @FXML
    private Label ErrorInEmail;

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
        bl = BusinessLogic.getInstance();
        logger.debug("Business logic initialized");
    }

    private void handleRegistration() {
        // Get form values
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean problem = false;

        Pattern Email = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher m = Email.matcher(email);
        boolean emailValid = m.matches();
        if (!emailValid) {
            ErrorInEmail.setText("Invalid email");
            ErrorInEmail.setStyle("-fx-text-fill: red;");
            logger.debug("Error in email, Invalid email");
            problem = true;
            }
        else{
            ErrorInEmail.setText("");
        }
        Pattern Password = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        m = Password.matcher(password);
        boolean passwordValid = m.matches();
        if (!passwordValid) {
            ErrorInPassword.setText("Password must contain at least 8 characters, including UPPER/lowercase and numbers");
            ErrorInPassword.setStyle("-fx-text-fill: red;");
            logger.debug("Error in password, Specification not met");
            problem = true;
        }
        else{
            ErrorInPassword.setText("");
        }
        if (problem) {
            return;
        }

        String confirmPassword = confirmPasswordField.getText();
        boolean termsAccepted = termsCheckbox.isSelected();

        logger.info("User tried to register. fullName=\"{}\", email=\"{}\", password=\"{}\", confirmPassword=\"{}\", termsAccepted=\"{}\"", fullName, email, password, confirmPassword, termsAccepted);

        // Check if the terms have been accepted (likely to be removed)
        if (!termsAccepted) {
            logger.info("User did not accept the terms and conditions. Registration aborted.");
            return;
        }

        // Call the business logic to register the user
        OvernoteUser user = bl.registerUser(fullName, email, password, confirmPassword);
        WindowManager.getInstance().navigateToMain();
    }

    private void navigateToLogin() {
        logger.info("User clicked on \"{}\"", signInButton.getText());
        WindowManager.getInstance().navigateToLogin();
    }
}