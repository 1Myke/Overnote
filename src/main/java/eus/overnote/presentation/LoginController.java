package eus.overnote.presentation;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

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

    @FXML
    private Pane barPane;

    // Variables for window dragging
    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() {
        // Set up event handlers for login functionality
        loginButton.setOnAction(event -> handleLoginButtonAction());
        logger.debug("\"" + loginButton.getText() + "\" button listener initialized");
        createAccountButton.setOnAction(event -> navigateToRegister());
        logger.debug("\"" + createAccountButton.getText() + "\" button listener initialized");
        forgotPasswordLabel.setOnMouseClicked(event -> handleForgotPasswordAction());
        logger.debug("\"" + forgotPasswordLabel.getText() + "\" label listener initialized");

        // Set up custom window control buttons
        closeButton.setOnAction(event -> {
            logger.info("Close button clicked");
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        minimizeButton.setOnAction(event -> {
            logger.info("Minimize button clicked");
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });

        // Set up window dragging
        barPane.setOnMousePressed(this::handleMousePressed);
        logger.debug("Mouse pressed listener initialized");
        barPane.setOnMouseDragged(this::handleMouseDragged);
        logger.debug("Mouse dragged listener initialized");
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();

        logger.debug("Mouse pressed at x: " + xOffset + ", y: " + yOffset);
    }

    private void handleMouseDragged(MouseEvent event) {
        logger.debug("Mouse dragged to x: " + event.getScreenX() + ", y: " + event.getScreenY());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void handleLoginButtonAction() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckbox.isSelected();

        logger.info(
                "User tried to log in. " +
                "email=\"" + email +
                "\", password=\"" + password+
                "\", rememberMe=\"" + rememberMe + "\""
        );
    }

    private void navigateToRegister() {
        logger.info("User clicked on \"" + createAccountButton.getText() + "\"");
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            // Add CSS for styling
            loginScene.getStylesheets().add(RegisterApplication.class.getResource("register.css").toExternalForm());
            loginScene.setFill(Color.TRANSPARENT);

            // Get the current stage and set the new scene
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.setScene(loginScene);

            logger.info("Navigated to register view");

        } catch (IOException e) {
            logger.error("Error changing to register view", e);
        }
    }

    private void handleForgotPasswordAction() {
        logger.info("User clicked on \"" + forgotPasswordLabel.getText() + "\"");
    }
}