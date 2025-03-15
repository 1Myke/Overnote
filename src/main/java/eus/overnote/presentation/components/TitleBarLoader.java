package eus.overnote.presentation.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class TitleBarLoader {
    public static Parent load(String title) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(TitleBarController.class.getResource("titlebar.fxml"));
        Parent titleBar = loader.load();

        // Get the controller
        TitleBarController controller = loader.getController();

        controller.setTitle(title);

        return titleBar;
    }
}
