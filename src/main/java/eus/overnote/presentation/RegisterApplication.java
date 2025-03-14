package eus.overnote.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(RegisterApplication.class.getResource("register.fxml"));
        Parent root = loader.load();

        // Get the controller to set up window dragging
        RegisterController controller = loader.getController();

        // Add CSS for styling
        Scene scene = new Scene(root);
        scene.getStylesheets().add(RegisterApplication.class.getResource("register.css").toExternalForm());

        // Remove default window decoration
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}