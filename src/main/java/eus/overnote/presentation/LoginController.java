package eus.overnote.presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;

public class LoginController {

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
        createAccountButton.setOnAction(event -> navigateToRegister());
        forgotPasswordLabel.setOnMouseClicked(event -> handleForgotPasswordAction());

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

    private void handleLoginButtonAction() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckbox.isSelected();

        System.out.println(
                "Email: " + email + "\n" +
                "Password: " + password + "\n" +
                "Remember me: " + rememberMe
        );
    }

    private void navigateToRegister() {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            // Add CSS for styling
            loginScene.getStylesheets().add(RegisterApplication.class.getResource("register.css").toExternalForm());
            loginScene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            // Get the current stage and set the new scene
            Stage stage = (Stage) createAccountButton.getScene().getWindow();
            stage.setScene(loginScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleForgotPasswordAction() {
        // TODO: Implementar la lógica para recuperar contraseña
        System.out.println("Recuperar contraseña");
    }
}