module eus.overnote.presentation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires com.h2database;

    opens eus.overnote.presentation to javafx.fxml;
    exports eus.overnote.presentation;
    exports eus.overnote.domain;
    opens eus.overnote.domain to javafx.fxml, org.hibernate.orm.core;
}