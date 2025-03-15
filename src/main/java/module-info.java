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

    opens eus.overnote.domain to org.hibernate.orm.core;
    opens eus.overnote.presentation to javafx.fxml;
    exports eus.overnote.presentation;
}