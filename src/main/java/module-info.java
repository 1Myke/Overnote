module eus.overnote.presentation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens eus.overnote.presentation to javafx.fxml;
    exports eus.overnote.presentation;
}