package eus.overnote.presentation.components;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitleBarController {

    private static final Logger logger = LoggerFactory.getLogger(TitleBarController.class);

    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private BorderPane barPane;

    @FXML
    private Label titleLabel;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() {
        // Set up event handlers for window functionality
        minimizeButton.setOnAction(event -> minimizeWindow());
        closeButton.setOnAction(event -> closeWindow());

        // Set up event handlers for window dragging
        barPane.setOnMousePressed(this::handleMousePressed);
        barPane.setOnMouseDragged(this::handleMouseDragged);
        barPane.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {
        // Reset bar style
        barPane.getStyleClass().remove("titlebar-dragged");
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();

        // Change bar style to indicate dragging
        barPane.getStyleClass().add("titlebar-dragged");

        logger.debug("Mouse pressed at x: {}, y: {}", xOffset, yOffset);
    }

    private void handleMouseDragged(MouseEvent event) {
        logger.debug("Mouse dragged to x: {}, y: {}", event.getScreenX(), event.getScreenY());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void minimizeWindow() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
