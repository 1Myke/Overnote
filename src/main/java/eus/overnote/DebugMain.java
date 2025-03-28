package eus.overnote;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.OvernoteUser;
import javafx.application.Application;

public class DebugMain extends Application {

    @Override
    public void start(javafx.stage.Stage stage) {
        BlInterface bl = BusinessLogic.getInstance();
        bl.registerUser("Debug User", "debug@overnote.eus", "debug", "debug");
        bl.loginUser("debug@overnote.eus", "debug");
        eus.overnote.presentation.WindowManager.getInstance().navigateToMain();
    }

    public static void main(String[] args) {
        launch();
    }
}
