package application;

import gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Controller controller;

    public static Controller getController() {
        return controller;
    }

    public static void restart(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("GUI.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Project Graph Java - S.Maliński, K.Krzychowiec");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
