package eus.overnote;

import eus.overnote.presentation.WindowManager;
import javafx.application.Application;
import eus.overnote.data_access.DbAccessManager;
import javafx.scene.image.Image;

import java.util.Objects;

public class Main extends Application {

    private static final DbAccessManager dbAccessManager = new DbAccessManager();

    @Override
    public void start(javafx.stage.Stage stage) {
        WindowManager.getInstance().openApplication();
    }

    @Override
    public void stop(){
        dbAccessManager.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
