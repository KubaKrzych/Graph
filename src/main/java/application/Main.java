package application;

import gui.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.cli.*;

public class Main extends Application {
    private static Controller controller;
    private static final CommandLineParser parser = new DefaultParser();
    public static boolean debug;
    private static final EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case W:
                    controller.moveUp();
                    break;
                case S:
                    controller.moveDown();
                    break;
                case A:
                    controller.moveLeft();
                    break;
                case D:
                    controller.moveRight();
                    break;
                case M:
                    controller.ZoomIn();
                    break;
                case N:
                    controller.ZoomOut();
                    break;
                case C:
                    if (debug)
                        controller.printAPLocation();
                    break;
                default:
                    event.consume();
            }

        }
    };


    public static Controller getController() {
        return controller;
    }

    public static void restart(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("GUI.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);
            scene.setOnKeyPressed(keyListener);
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
            scene.setOnKeyPressed(keyListener);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Graph Project, by S.Maliński, K.Krzychowiec");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            stage.close();
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        Option debugOpt = new Option("d", "debug", false, "debug mode for devs");
        debugOpt.setRequired(false);
        options.addOption(debugOpt);
        try {
            CommandLine cmd = parser.parse(options, args);
            debug = cmd.hasOption("d");
        } catch (ParseException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }
        launch(args);
    }
}
