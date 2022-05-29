module javafx.maven.setup {
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    exports application;
    exports gui;
    opens gui;
}