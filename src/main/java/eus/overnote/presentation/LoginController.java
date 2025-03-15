package eus.overnote.presentation;


import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.factories.WindowFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        bl = new BusinessLogic();
        logger.debug("Business logic initialized");
    }

    private void handleLoginButtonAction() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckbox.isSelected();

        logger.info("User tried to log in. email=\"{}\", password=\"{}\", rememberMe=\"{}\"", email, password, rememberMe);

        // Call the business logic to log in the user
        bl.loginUser(email, password);
    }

    private void navigateToRegister() {
        logger.info("User clicked on \"" + createAccountButton.getText() + "\"");
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("register.fxml"));
            Pane loginContent = loader.load();

            // Create register window using the factory
            Stage registerWindow = WindowFactory.createWindow("Overnote - Register", loginContent);

            // Close the login window
            Stage loginWindow = (Stage) createAccountButton.getScene().getWindow();
            loginWindow.close();

            // Show the window
            registerWindow.show();

            logger.info("Navigated to register view");
        } catch (IOException e) {
            logger.error("Error changing to register view", e);
        }
    }

    private void handleForgotPasswordAction() {
        logger.info("User clicked on \"" + forgotPasswordLabel.getText() + "\"");
    }
}