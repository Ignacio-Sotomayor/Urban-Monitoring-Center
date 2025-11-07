module com.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;
    requires jdk.jsobject;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;


    opens com.view to javafx.fxml;
    exports com.view;
    exports com.view.Reports;
    opens com.view.Reports to javafx.fxml;
    exports com.controller;
    opens com.controller to javafx.fxml;
}