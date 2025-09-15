module com.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires org.jetbrains.annotations;

    opens com.view to javafx.fxml;
    exports com.view;
}