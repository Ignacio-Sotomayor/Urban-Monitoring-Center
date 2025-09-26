module com.view {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;

    opens com.view to javafx.fxml;
    exports com.view;
}