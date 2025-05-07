module eus.overnote.presentation {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires javafx.base;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires com.h2database;
    requires jbcrypt;
    requires org.jsoup;
    requires org.fxmisc.richtext;

    exports eus.overnote;
    exports eus.overnote.presentation;
    exports eus.overnote.presentation.components;
    exports eus.overnote.factories;
    exports eus.overnote.domain;
    exports eus.overnote.presentation.views;

    opens eus.overnote.domain to org.hibernate.orm.core;
    opens eus.overnote.presentation to javafx.fxml;
    opens eus.overnote.presentation.components to javafx.fxml;
    opens eus.overnote.presentation.views to javafx.fxml;
}