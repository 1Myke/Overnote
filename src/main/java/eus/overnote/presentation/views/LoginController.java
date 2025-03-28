package eus.overnote.presentation.views;


import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.presentation.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    BlInterface bl;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckbox;

    @FXML
    private Label forgotPasswordLabel;

    @FXML
    private Button loginButton;

    public void initialize() {
        // Set up event handlers for login functionality
        loginButton.setOnAction(event -> handleLoginButtonAction());
        logger.debug("\"{}\" button listener initialized", loginButton.getText());
        createAccountButton.setOnAction(event -> navigateToRegister());
        logger.debug("\"{}\" button listener initialized", createAccountButton.getText());
        forgotPasswordLabel.setOnMouseClicked(event -> handleForgotPasswordAction());
        logger.debug("\"{}\" label listener initialized", forgotPasswordLabel.getText());

        // Initialize business logic
        bl = BusinessLogic.getInstance();
        logger.debug("Business logic initialized");
    }

    private void handleLoginButtonAction() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckbox.isSelected();

        logger.info("User tried to log in. email=\"{}\", password=\"{}\", rememberMe=\"{}\"", email, password, rememberMe);

        // Call the business logic to log in the user
        bl.loginUser(email, password);
        WindowManager.getInstance().navigateToMain();
    }

    private void navigateToRegister() {
        logger.info("User clicked on \"{}\"", createAccountButton.getText());
        WindowManager.getInstance().navigateToRegister();
    }

    private void handleForgotPasswordAction() {
        logger.info("User clicked on \"" + forgotPasswordLabel.getText() + "\"");
    }
}