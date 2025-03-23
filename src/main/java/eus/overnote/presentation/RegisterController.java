package eus.overnote.presentation;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private Pane barPane;

    // Variables for window dragging
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Set up event handlers for registration functionality
        registerButton.setOnAction(event -> handleRegistration());
        logger.debug("\"{}\" button listener initialized", registerButton.getText());
        signInButton.setOnAction(event -> navigateToLogin());
        logger.debug("\"{}\" button listener initialized", signInButton.getText());

        // Set up custom window control buttons
        closeButton.setOnAction(event -> {
            logger.debug("Close button clicked");
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        minimizeButton.setOnAction(event -> {
            logger.debug("Minimize button clicked");
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });

        // Set up window dragging
        barPane.setOnMousePressed(this::handleMousePressed);
        logger.debug("Mouse pressed listener initialized");
        barPane.setOnMouseDragged(this::handleMouseDragged);
        logger.debug("Mouse dragged listener initialized");

        // Initialize business logic
        bl = BusinessLogic.getInstance();
        logger.debug("Business logic initialized");
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
        logger.debug("Mouse pressed at ({}, {})", xOffset, yOffset);
    }

    private void handleMouseDragged(MouseEvent event) {
        logger.debug("Mouse dragged to x: {}, y: {}", event.getScreenX(), event.getScreenY());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
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
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            // Add CSS for styling
            loginScene.getStylesheets().add(RegisterApplication.class.getResource("login.css").toExternalForm());
            loginScene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            // Get the current stage and set the new scene
            Stage stage = (Stage) signInButton.getScene().getWindow();
            stage.setScene(loginScene);
            logger.info("Navigated to login screen");
        } catch (IOException e) {
            logger.error("Failed to navigate to login screen", e);
        }
    }
}