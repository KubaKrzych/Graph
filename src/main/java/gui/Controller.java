package gui;

import application.Main;
import graph.Graph;
import graph.ModeOfGraph;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static int mode = 0;
    private static Graph graph;
    private static double zoomDelta;
    private PauseTransition pauseForZoomSlider;
    private Service service;

    @FXML
    Button B_Open;
    @FXML
    Button B_Save;
    @FXML
    Button B_Generate;
    @FXML
    Button B_Reset;
    @FXML
    Button B_Add_path;
    @FXML
    Button B_RemoveStart;

    @FXML
    TextField TF_Columns, TF_Rows;
    @FXML
    TextField TF_Lower_wage, TF_Upper_wage;
    @FXML
    TextField TF_From, TF_To;

    @FXML
    Label L_Komunikaty;

    @FXML
    RadioButton RB_all_rand;
    @FXML
    RadioButton RB_edge_rand;
    @FXML
    RadioButton RB_wage_rand;
    @FXML
    RadioButton RB_BFS, RB_Dijkstra;

    @FXML
    Slider S_zoom;

    @FXML
    AnchorPane AP_Paths;

    @FXML
    AnchorPane AP_GraphDisplay;

    @FXML
    ScrollPane P_GraphFrame;

    @FXML
    Pane P_AlgorithmsVisualtisationFrame;
    @FXML
    Pane P_GenrateOptionFrame;

    public void setGeneratorMode(ActionEvent Event) {
        RadioButton RB = (RadioButton) Event.getSource();
        if (RB == RB_all_rand) {
            mode = 0;
            RB_edge_rand.setSelected(false);
            RB_wage_rand.setSelected(false);
        } else if (RB == RB_edge_rand) {
            mode = 1;
            RB_all_rand.setSelected(false);
            RB_wage_rand.setSelected(false);
        } else if (RB == RB_wage_rand) {
            mode = 2;
            RB_all_rand.setSelected(false);
            RB_edge_rand.setSelected(false);
        }
    }

    public void setVisualisationMode(ActionEvent Event) {
        boolean BFS = true, Dijkstra = false;
        RadioButton RB = (RadioButton) Event.getSource();
        if (ListElement.getPathStart() == -1) {
            RB.setSelected(false);
        } else if (RB == RB_BFS & RB.isSelected()) {
            RB_Dijkstra.setSelected(false);
            GraphView.updateNodesColorByWage(graph.getWeightsToNodes(ListElement.getPathStart(), BFS));
        } else if (RB == RB_Dijkstra & RB.isSelected()) {
            RB_BFS.setSelected(false);
            GraphView.updateNodesColorByWage(graph.getWeightsToNodes(ListElement.getPathStart(), Dijkstra));
        } else {
            GraphView.updateNodesColorByWage();
        }
    }

    public void OpenGraphFromFile(ActionEvent Event) {
        printMessage(0);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Stage stage = (Stage) ((Node) Event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (graph == null) graph = new Graph();

        if (selectedFile != null) {
            try {
                graph.readGraphFromFile(selectedFile.getAbsolutePath());
            } catch (IOException exception) {
                printMessage(13);
                return;
            }
            setNewGraphView();
        } else {
            printMessage(3);
        }
    }

    public void SaveGraphToFile(ActionEvent event) {
        printMessage(0);
        if (graph != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a destination folder");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    graph.saveGraphToFile(file.getAbsolutePath());
                } catch (IOException exception) {
                    printMessage(14);
                }
            }
        }
    }

    public void Reset(ActionEvent Event) {
        Main.restart((Stage) ((Node) Event.getSource()).getScene().getWindow());
    }

    public void addPathToVisualisation(ActionEvent ignoreEvent) {
        printMessage(0);
        if (!ListElement.canAddNewPath()) {
            printMessage(9);
            return;
        }
        int start = getStartFromTextField();
        int end = getEndFromTextField();
        if (start == -1 || end == -1) return;
        disableFromTextField(true);
        if (ListElement.doesPathExist(end)) {
            printMessage(10);
            return;
        }
        new ListElement(start, end);
    }

    public void RemoveStart(ActionEvent Event) {
        ListElement.Reset();
    }

    public void CreateNewGraph(ActionEvent ignoreEvent) {
        printMessage(0);
        if (!AP_GraphDisplay.getChildren().isEmpty()) {
            AP_GraphDisplay.getChildren().removeAll();
        }
        double minWage = getMinWageFromTextField();
        double maxWage = getMaxWageFromTextField();
        int cols = getColsFromTextField();
        int rows = getRowsFromTextFields();
        if (cols == -1 || rows == -1 || minWage == -1 || maxWage == -1) return;
        if (minWage > maxWage) {
            printMessage(8);
            return;
        }
        ListElement.Reset();
        S_zoom.setValue(0);
        B_Generate.setDisable(true);
        service = new Service<>() {
            @Override
            public Task<Object> createTask() {
                return new Task<Object>() {
                    @Override
                    protected Object call() throws Exception {
                        setDisableRadioButtonsForVisualisation(true);
                        GraphGenerate(cols, rows, minWage, maxWage);
                        Platform.runLater(() -> setNewGraphView());
                        Thread.sleep(2000);
                        B_Generate.setDisable(false);
                        return null;
                    }
                };
            }

        };
        service.start();
    }

    private void setNewGraphView() {
        new GraphView(graph);

        AP_GraphDisplay.getChildren().add(GraphView.getPane());
        AP_GraphDisplay.setPrefSize(400 / GraphView.getPane().getPrefWidth(), 400 / GraphView.getPane().getPrefHeight());
        AP_GraphDisplay.setScaleY(400 / GraphView.getPane().getPrefWidth());
        AP_GraphDisplay.setScaleX(400 / GraphView.getPane().getPrefWidth());
        P_GraphFrame.setPrefSize(400, 400);
        P_GraphFrame.setContent(AP_GraphDisplay);
        zoomDelta = 400 / GraphView.getPane().getPrefWidth();
    }

    private void GraphGenerate(int cols, int rows, double minWage, double maxWage) {
        graph = new Graph();
        graph.setNumberOfColumns(cols);
        graph.setNumberOfRows(rows);
        graph.setMinWeightOfEdge(minWage);
        graph.setMaxWeightOfEdge(maxWage);
        graph.generateNewGraph(getModeEnumFromIdNumber(mode));
    }

    private double getMinWageFromTextField() {
        double minWage;
        try {
            minWage = Double.parseDouble(TF_Lower_wage.getText());
        } catch (Exception NumberFormatException) {
            printMessage(7);
            ErrorEffectForTextField(TF_Lower_wage);
            return -1;
        }
        if (minWage < 0.0001 || minWage >= 10000 || minWage * 10000000 % 1000 != 0) {
            printMessage(1);
            return -1;
        }
        return minWage;
    }

    private double getMaxWageFromTextField() {
        double maxWage;
        try {
            maxWage = Double.parseDouble(TF_Upper_wage.getText());
        } catch (Exception NumberFormatException) {
            printMessage(7);
            ErrorEffectForTextField(TF_Upper_wage);
            return -1;
        }
        if (maxWage < 1 || maxWage >= 10000 || maxWage * 10000000 % 1000 != 0) {
            printMessage(1);
            return -1;
        }
        return maxWage;
    }

    private int getColsFromTextField() {
        int cols;
        try {
            cols = Integer.parseInt(TF_Columns.getText());
        } catch (Exception NumberFormatException) {
            printMessage(7);
            ErrorEffectForTextField(TF_Columns);
            return -1;
        }
        if (cols < 1 || cols >= 400) {
            printMessage(2);
            return -1;
        }
        return cols;
    }

    private int getRowsFromTextFields() {
        int rows;
        try {
            rows = Integer.parseInt(TF_Rows.getText());
        } catch (Exception NumberFormatException) {
            printMessage(7);
            ErrorEffectForTextField(TF_Rows);
            return -1;
        }
        if (rows < 1 || rows >= 400) {
            printMessage(2);
            return -1;
        }
        return rows;
    }

    private int getStartFromTextField() {
        int start;
        try {
            start = Integer.parseInt(TF_From.getText());
        } catch (Exception NumberFormatException) {
            printMessage(7);
            ErrorEffectForTextField(TF_From);
            return -1;
        }
        if (start < 0 || start > graph.getNumberOfColumns() * graph.getNumberOfRows() - 1) {
            printMessage(11);
            return -1;
        }
        return start;
    }

    private int getEndFromTextField() {
        int end;
        try {
            end = Integer.parseInt(TF_To.getText());
        } catch (Exception NumberFormatException) {
            printMessage(7);
            ErrorEffectForTextField(TF_To);
            return -1;
        }
        if (end < 0 || end > graph.getNumberOfColumns() * graph.getNumberOfRows() - 1) {
            printMessage(11);
            return -1;
        }
        return end;
    }

    public void setDisableRadioButtonsForVisualisation(boolean value) {
        if (!value) {
            RB_BFS.setSelected(false);
            RB_Dijkstra.setSelected(false);
        }
        RB_BFS.setDisable(value);
        RB_Dijkstra.setDisable(value);
    }

    private ModeOfGraph getModeEnumFromIdNumber(int mode) {
        switch (mode) {
            case 0:
                return ModeOfGraph.ALL_RAND;
            case 1:
                return ModeOfGraph.EDGE_RAND;
            case 2:
                return ModeOfGraph.WAGE_RAND;
            default:
                return ModeOfGraph.BLANK;
        }
    }

    public void disableFromTextField(boolean value) {
        TF_From.setDisable(value);
    }

    public void ErrorEffectForTextField(TextField textField) {
        BackgroundFill filWhite = new BackgroundFill(Color.WHITE, null, null);
        textField.setBackground(new Background(new BackgroundFill(Color.rgb(255, 186, 186), null, null)));
        delay(1000, () -> textField.setBackground(new Background(filWhite)));
    }

    public static void delay(long milis, Runnable countinuation) {
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(milis);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> countinuation.run());
        new Thread(sleeper).start();
    }

    public void printMessage(int id) {
        switch (id) {
            case 0:
                L_Komunikaty.setText("");
                break;
            case 1:
                L_Komunikaty.setText(" 1. Komunikat : Niepoprawna waga przedziału wag.");
                ErrorEffectForTextField(TF_Lower_wage);
                ErrorEffectForTextField(TF_Upper_wage);
                break;
            case 2:
                L_Komunikaty.setText(" 2. Komunikat : Niepoprawna liczba wierszy, lub kolumn.");
                ErrorEffectForTextField(TF_Columns);
                ErrorEffectForTextField(TF_Rows);
                break;
            case 3:
                L_Komunikaty.setText(" 3. Komunikat : Błąd przy próbie czytania z pliku.");
                break;
            case 4:
                L_Komunikaty.setText(" 4. Komunikat : Niepoprawna struktura pliku wejściowego.");
                break;
            case 5:
                L_Komunikaty.setText(" 5. Komunikat : Podano nieodpowiednią nazwę trybu generowanie grafu.");
                break;
            case 6:
                L_Komunikaty.setText(" 6. Komunikat : Nie można zapisać grafu do pliku.");
                break;
            case 7:
                L_Komunikaty.setText(" 7. Komunikat : Niepoprawne argumenty wejściowe.");
                break;
            case 8:
                L_Komunikaty.setText(" 8. Komunikat : Podano nieprawidłowy przedział wag.");
                ErrorEffectForTextField(TF_Upper_wage);
                ErrorEffectForTextField(TF_Lower_wage);
                break;
            case 9:
                L_Komunikaty.setText(" 9. Komunikat : Nie można dodać więcej dróg.");
                ErrorEffectForTextField(TF_From);
                ErrorEffectForTextField(TF_To);
                break;
            case 10:
                L_Komunikaty.setText(" 10. Komunikat : Droga do tego wierzchołka już istnieje.");
                ErrorEffectForTextField(TF_To);
                break;
            case 11:
                L_Komunikaty.setText(" 11. Komunikat : Podany wierzchołek nie istnieje.");
                break;
            case 12:
                L_Komunikaty.setText(" 12. Komunikat : Nie istnieje droga między podanymi wierzchołkami.");
                break;
            case 13:
                L_Komunikaty.setText(" 13. Komunikat : Podany plik zawiera niepoprawną strukturę.");
                break;
            case 14:
                L_Komunikaty.setText(" 14. Komunikat : Nie można zapisać grafu do pliku.");
                break;
            default:
                break;
        }
    }

    private void frame(Pane pane) {
        pane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        frame(P_AlgorithmsVisualtisationFrame);
        frame(P_GenrateOptionFrame);
        pauseForZoomSlider = new PauseTransition(Duration.seconds(0.5));
        pauseForZoomSlider.setOnFinished(event -> {
            new Thread(() -> {
                double pocz = 400 / GraphView.getPane().getPrefWidth();
                double kon = 1;
                double scale = pocz + (S_zoom.getValue() / 100) * (kon - pocz);
                zoomDelta = scale;
                scaleGraph(scale);
                moveRight();
                moveDown();
            }).start();
        });
        S_zoom.valueProperty().addListener((observableValue, number, t1) -> {
            pauseForZoomSlider.playFromStart();
        });
    }

    private void scaleGraph(double zoom) {
        AP_GraphDisplay.setScaleY(zoom);
        AP_GraphDisplay.setScaleX(zoom);
    }

    public Graph getGraph() {
        return graph;
    }

    public void moveUp() {
        Pane pane = AP_GraphDisplay;
        double transeltionY = pane.getTranslateY();
        if (transeltionY <= -10) pane.setTranslateY(transeltionY + 10);
    }

    public void moveDown() {
        Pane pane = AP_GraphDisplay;
        double translateY = pane.getTranslateY();
        double graphLength = 400 / pane.getPrefHeight() * zoomDelta;
        double frameLength = 400;
        double maxTranslate = graphLength - frameLength;
        if (maxTranslate + translateY > 10) pane.setTranslateY(translateY - 10);
        else if (maxTranslate + translateY != 0) pane.setTranslateY(-maxTranslate);
    }

    public void moveLeft() {
        Pane pane = AP_GraphDisplay;
        double transeltionX = pane.getTranslateX();
        if (transeltionX <= -10) pane.setTranslateX(transeltionX + 10);
    }

    public void moveRight() {
        Pane pane = AP_GraphDisplay;
        double transeltionX = pane.getTranslateX();
        double graphLength = 400 / pane.getPrefHeight() * zoomDelta;
        double frameLength = 400;
        double maxTransletion = graphLength - frameLength;
        if (maxTransletion + transeltionX > 10) pane.setTranslateX(transeltionX - 10);
        else if (maxTransletion + transeltionX != 0) pane.setTranslateX(-maxTransletion);
    }

    public void ZoomIn() {
        double value = S_zoom.getValue();
        if (value <= 90) {
            S_zoom.setValue(value + 10);
        }
    }

    public void ZoomOut() {
        double value = S_zoom.getValue();
        if (value >= 10) {
            S_zoom.setValue(value - 10);
        }
    }

    public void printAPLocation() {
        Pane pane = AP_GraphDisplay;
        double translateX = pane.getTranslateX();
        double graphLength = 400 / pane.getPrefHeight();
        double frameLength = graphLength * zoomDelta;
        double maxTranslate = graphLength - frameLength;
        if (Main.debug)
            System.out.println("TranselrionX = " + translateX + " GraphLength = " + graphLength + " frameLength = " + frameLength + " maxTransletion = " + maxTranslate + " zoomDelta = " + zoomDelta);
    }
}
