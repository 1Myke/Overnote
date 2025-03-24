package eus.overnote;

import eus.overnote.presentation.WindowManager;
import javafx.application.Application;

public class Main extends Application {

    @Override
    public void start(javafx.stage.Stage stage) {
        WindowManager.getInstance().navigateToLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}
