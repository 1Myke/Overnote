package eus.overnote;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import javafx.application.Application;

import java.util.Locale;

public class DebugMain extends Application {

    @Override
    public void start(javafx.stage.Stage stage) {
        // Set the locale for debugging
        Locale.setDefault(new Locale("es", "ES"));

        BlInterface bl = BusinessLogic.getInstance();
        bl.registerUser("Debug User", "debug@overnote.eus", "Debug12345?", "Debug12345?");
        bl.loginUser("debug@overnote.eus", "Debug12345?", true);
        eus.overnote.presentation.WindowManager.getInstance().navigateToMain();
    }

    public static void main(String[] args) {
        launch();
    }
}
