module javafx.maven.setup {
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires commons.cli;
    exports application;
    exports gui;
    opens gui;
}