package eus.overnote.presentation.components;

import eus.overnote.businesslogic.BusinessLogic;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ProfileBannerController {
    BusinessLogic bl = BusinessLogic.getInstance();

    @FXML
    Text nameText;
    
    public void initialize() {
        // Set the user's name to the current logged-in user's name
        nameText.setText(bl.getLoggedInUser().getFullName());
    }
}
