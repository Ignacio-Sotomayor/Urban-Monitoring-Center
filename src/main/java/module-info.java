module com.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;
    requires jdk.jsobject;
    requires org.jetbrains.annotations;
    requires java.desktop;

    opens com.view to javafx.fxml;
    exports com.view;
}