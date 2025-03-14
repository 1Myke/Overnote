package eus.overnote.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegisterController {

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
        signInButton.setOnAction(event -> navigateToLogin());

        // Set up custom window control buttons
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });

        // Set up window dragging
        barPane.setOnMousePressed(this::handleMousePressed);
        barPane.setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
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

        System.out.println(
            "Full Name: " + fullName + "\n" +
            "Email: " + email + "\n" +
            "Password: " + password + "\n" +
            "Confirm Password: " + confirmPassword + "\n" +
            "Terms Accepted: " + termsAccepted
        );
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}