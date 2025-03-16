package eus.overnote.presentation.components;

import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CustomWindowController {

    private static final Logger logger = LoggerFactory.getLogger(CustomWindowController.class);

    @FXML
    private BorderPane root;

    @FXML
    private BorderPane container;

    @FXML
    private BorderPane barPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private Pane topLeftResize, topRightResize, bottomLeftResize, bottomRightResize,
            topResize, bottomResize, leftResize, rightResize;

    @FXML
    private HBox topResizeHBox, bottomResizeHBox;

    private List<Node> resizePanes;

    private boolean justUnmaximized = false;
    private Dimension2D dimBeforeUnmax;
    private Point2D mouseBeforeUnmax;
    
    private double xOffset = 0;
    private double yOffset = 0;

    Stage stage;
    private double dragStartScreenX;
    private double dragStartScreenY;
    private double targetStageWidth;
    private double targetStageHeight;

    private Rectangle windowClip;

    @Getter
    private boolean resizable = true;

    public void initialize() {
        // Set up window clipping
        windowClip = new Rectangle();
        windowClip.widthProperty().bind(container.widthProperty());
        windowClip.heightProperty().bind(container.heightProperty());
        windowClip.setArcWidth(10);
        windowClip.setArcHeight(10);
        container.setClip(windowClip);

        // Set up event handlers for window functionality
        minimizeButton.setOnAction(event -> minimizeWindow());
        closeButton.setOnAction(event -> closeWindow());
        maximizeButton.setOnAction(event -> toggleMaximizeWindow());

        // Set up event handlers for window dragging
        barPane.setOnMousePressed(this::handleMousePressed);
        barPane.setOnMouseDragged(this::handleMouseDragged);
        barPane.setOnMouseReleased(this::handleMouseReleased);
        barPane.setOnMouseClicked(this::handleMouseClicked);

        // Create resize panes list
        resizePanes = List.of(topLeftResize, topRightResize, bottomLeftResize, bottomRightResize,
                topResize, bottomResize, leftResize, rightResize, topResizeHBox, bottomResizeHBox);

        // Set up resize handlers
        setResizeHandlers(topLeftResize, DragDirection.NW);
        setResizeHandlers(topRightResize, DragDirection.NE);
        setResizeHandlers(bottomLeftResize, DragDirection.SW);
        setResizeHandlers(bottomRightResize, DragDirection.SE);
        setResizeHandlers(topResize, DragDirection.N);
        setResizeHandlers(bottomResize, DragDirection.S);
        setResizeHandlers(leftResize, DragDirection.W);
        setResizeHandlers(rightResize, DragDirection.E);
        setResizeHandlers(topResizeHBox, DragDirection.N);
        setResizeHandlers(bottomResizeHBox, DragDirection.S);
    }

    private void setResizeHandlers(Pane pane, DragDirection dragDirection) {
        pane.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                stage = (Stage) container.getCenter().getScene().getWindow();
                dragStartScreenX = event.getScreenX();
                dragStartScreenY = event.getScreenY();
                targetStageWidth = stage.getWidth();
                targetStageHeight = stage.getHeight();
                event.consume();
            }
        });

        pane.setOnMouseDragged(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }

            double mouseScreenX = event.getScreenX();
            double mouseScreenY = event.getScreenY();
            double deltaX = mouseScreenX - dragStartScreenX;
            double deltaY = mouseScreenY - dragStartScreenY;
            dragStartScreenX = mouseScreenX;
            dragStartScreenY = mouseScreenY;

            if (deltaX == 0 && deltaY ==0) {
                return;
            }

            logger.debug("mouseScreenX={} mouseScreenY={}\ndeltaX={} deltaY={} stageX={} stageY={} " +
                            "stageW={} stageH={} sceneX={} sceneY={} targetStageWidth={} targetStageHeight={}",
                    mouseScreenX, mouseScreenY, deltaX, deltaY, stage.getX(), stage.getY(),
                    stage.getWidth(), stage.getHeight(), event.getSceneX(), event.getSceneY(),
                    targetStageWidth,targetStageHeight);

            switch (dragDirection) {
                case N:
                    if (mouseScreenY < stage.getY() + stage.getHeight() - stage.getMinHeight()) {
                        stage.setY(mouseScreenY);
                        resizeStageHeight(-deltaY);
                    }
                    break;
                case NE:
                    if (mouseScreenY < stage.getY() + stage.getHeight() - stage.getMinHeight()) {
                        stage.setY(mouseScreenY);
                        resizeStageHeight(-deltaY);
                    }
                    resizeStageWidth(deltaX);
                    break;
                case E:
                    resizeStageWidth(deltaX);
                    break;
                case SE:
                    resizeStageHeight(deltaY);
                    resizeStageWidth(deltaX);
                    break;
                case S:
                    resizeStageHeight(deltaY);
                    break;
                case SW:
                    if (mouseScreenX < stage.getX() + stage.getWidth() - stage.getMinWidth()) {
                        stage.setX(mouseScreenX);
                        resizeStageWidth(-deltaX);
                    }
                    resizeStageHeight(deltaY);
                    break;
                case W:
                    if (mouseScreenX < stage.getX() + stage.getWidth() - stage.getMinWidth()) {
                        stage.setX(mouseScreenX);
                        resizeStageWidth(-deltaX);
                    }
                    break;
                case NW:
                    if (mouseScreenY < stage.getY() + stage.getHeight() - stage.getMinHeight()) {
                        stage.setY(mouseScreenY);
                        resizeStageHeight(-deltaY);
                    }
                    if (mouseScreenX < stage.getX() + stage.getWidth() - stage.getMinWidth()) {
                        stage.setX(mouseScreenX);
                        resizeStageWidth(-deltaX);
                    }
                    break;
            }
            event.consume();
        });
    }

    private void resizeStageWidth(double deltaX) {
        double newWidth = targetStageWidth + deltaX;

        if (newWidth >= stage.getMinWidth() && newWidth <= stage.getMaxWidth()) {
            targetStageWidth = newWidth;
            stage.setWidth(newWidth);
            stage.setHeight(targetStageHeight);
        }
    }

    private void resizeStageHeight(double deltaY) {
        double newHeight = targetStageHeight + deltaY;

        if (newHeight >= stage.getMinHeight() && newHeight <= stage.getMaxHeight()) {
            targetStageHeight = newHeight;
            stage.setHeight(newHeight);
            stage.setWidth(targetStageWidth);
        }
    }

    private enum DragDirection {
        N, NE, E, SE, S, SW, W, NW
    }

    private void handleMouseClicked(MouseEvent mouseEvent) {
        // Maximizing on double click
        if (mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2
        ) {
            toggleMaximizeWindow();
        }
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
        logger.debug("Offset set to x: {}, y: {}", xOffset, yOffset);
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Unmaximize and set the offset to match the dragged position
        if (stage.isMaximized()) {
            toggleMaximizeWindow();
            justUnmaximized = true;
            dimBeforeUnmax = new Dimension2D(stage.getWidth(), stage.getHeight());
            mouseBeforeUnmax = new Point2D(event.getSceneX(), event.getSceneY());

            logger.debug("Window is maximized: Unmaximizing");
            logger.debug("Dimensions before unmaximize: width: {}, height: {}", dimBeforeUnmax.getWidth(), dimBeforeUnmax.getHeight());
            logger.debug("Mouse before unmaximize: x: {}, y: {}", mouseBeforeUnmax.getX(), mouseBeforeUnmax.getY());
        } else {
            logger.debug("Window is already unmaximized");
        }

        if (justUnmaximized &&
            stage.getWidth() != dimBeforeUnmax.getWidth() &&
            stage.getHeight() != dimBeforeUnmax.getHeight()
        ) {
            logger.debug("Mouse dragged at x: {}, y: {}", event.getSceneX(), event.getSceneY());
            logger.debug("Global mouse dragged at x: {}, y: {}", event.getScreenX(), event.getScreenY());

            justUnmaximized = false;
            xOffset = stage.getWidth()*mouseBeforeUnmax.getX()/dimBeforeUnmax.getWidth();

            logger.debug("Offset set to x: {}, y: {}", xOffset, yOffset);
        }

        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);

        logger.debug("Window moved to x: {}, y: {}", stage.getX(), stage.getY());
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

        logger.debug("Window closed");
    }

    private void toggleMaximizeWindow() {
        // Check if window is resizable
        if (!isResizable()) return;

        Stage stage = (Stage) maximizeButton.getScene().getWindow();
        boolean isNowMaximized = !stage.isMaximized();
        stage.setMaximized(isNowMaximized);

        resizePanes.forEach(
            pane -> {
                pane.setVisible(!isNowMaximized);
                pane.setManaged(!isNowMaximized);
            });

        logger.debug("Maximized: {}", isNowMaximized);
    }

    private void minimizeWindow() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);

        logger.debug("Window iconified");
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setContent(Pane content) {
        this.container.setCenter(content);
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;

        // Set resize panes visibility and managed properties
        resizePanes.forEach(pane -> {
            pane.setVisible(resizable);
            pane.setManaged(resizable);
        });
        maximizeButton.setVisible(resizable);
        maximizeButton.setManaged(resizable);

        logger.debug("Window resizable set to: {}", resizable);
    }
}
