package gui;

import application.Main;
import graph.IncoherentException;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.Queue;


public class ListElement {
    private final static int maxNumberOfPaths = 10;
    private static int numberOfPaths = 0;
    private static int PathStart = -1;

    private static final ListElement[] listElements = new ListElement[maxNumberOfPaths];

    private static final int heightOfLabel = 25;
    private int id;
    private int PathEnd;
    private boolean isVisible;
    private Pane P_pane;
    private Label L_content;
    private CheckBox C_show;
    private Button B_delete;

    private int size = 10;
    private Line[] pathView = new Line[size];
    private int licznik = 0;

    public ListElement(int start, int end) {
        Controller controller = Main.getController();
        if (doesPathExist(end)) {
            Main.getController().printMessage(10);
            return;
        }
        if (PathStart == end)
            return;
        if (PathStart == -1) {
            setPathStart(start);
            controller.disableFromTextField(true);
            GraphView.getNodeView(PathStart).selectColor();
            controller.setDisableRadioButtonsForVisualisation(false);
        }
        Queue<Integer> queueOfPath;
        try {
            queueOfPath = Main.getController().getGraph().getPath(PathStart, end, false);
        } catch (IncoherentException e) {
            Main.getController().printMessage(12);
            return;
        }
        this.PathEnd = end;
        isVisible = true;
        id = numberOfPaths;
        L_content = new Label(" " + id + ". " + PathStart + "-->" + end);
        L_content.setLayoutX(0);
        L_content.setLayoutY(4);
        C_show = new CheckBox();
        C_show.setLayoutX(318);
        C_show.setLayoutY(4);
        C_show.setSelected(true); //346 0
        C_show.setOnAction(event -> showPath(isVisible = !isVisible));
        B_delete = new Button("X");
        B_delete.setLayoutX(346);
        B_delete.setLayoutY(0);
        B_delete.setOnAction(event -> deleteElement());
        P_pane = new Pane();
        P_pane.setPrefSize(373, 25);
        P_pane.getChildren().add(C_show);
        P_pane.getChildren().add(L_content);
        P_pane.getChildren().add(B_delete);
        P_pane.setLayoutX(0);
        P_pane.setLayoutY(id * heightOfLabel);
        P_pane.setVisible(true);
        listElements[numberOfPaths++] = this;

        GraphView.getNodeView(end).selectColor();
        GraphView.getNodeView(end).setListElement(this);
        newPathView(GraphView.getColorFromScale(10, 0, numberOfPaths), queueOfPath);
        controller.AP_Paths.getChildren().add(P_pane);
        controller.AP_GraphDisplay.getChildren().addAll(pathView);

    }

    private void newPathView(Color pathViewColor, Queue<Integer> queueOfPath) {
        this.isVisible = true;
        while (!queueOfPath.isEmpty()) {
            if (this.licznik >= size)
                DoubleMemoryForPath();
            int idF = queueOfPath.peek();
            queueOfPath.remove();
            if (queueOfPath.isEmpty())
                break;
            int idL = queueOfPath.peek();
            Line line = new Line(GraphView.getNodeView(idF).getLayoutX(),
                    GraphView.getNodeView(idF).getLayoutY(),
                    GraphView.getNodeView(idL).getLayoutX(),
                    GraphView.getNodeView(idL).getLayoutY());
            line.setStroke(pathViewColor);
            line.setStrokeWidth(5);
            line.setVisible(true);
            pathView[licznik++] = line;
        }
        TrimAllocatedMemoryForPath();
    }

    private void DoubleMemoryForPath() {
        Line[] newPath = new Line[2 * size];
        size = 2 * size;
        System.arraycopy(pathView, 0, newPath, 0, licznik);
        pathView = newPath;
    }

    private void TrimAllocatedMemoryForPath() {
        Line[] newPath = new Line[licznik];
        size = licznik;
        System.arraycopy(pathView, 0, newPath, 0, licznik);
        pathView = newPath;
    }

    public void showPath(boolean show) {
        for (int i = 0; i < pathView.length; i++)
            pathView[i].setVisible(show);
    }

    public void deleteElement() {
        P_pane.setVisible(false);
        System.arraycopy(listElements, id + 1, listElements, id, maxNumberOfPaths - 1 - id);
        for (int i = id; i < numberOfPaths - 1; i++)
            listElements[i].setId(i);
        numberOfPaths--;
        update();
        if (numberOfPaths == 0) {
            setPathStart(-1);
            GraphView.updateNodesColorByWage();
        } else {
            GraphView.getNodeView(PathEnd).unselectColor();
        }
        showPath(false);
    }

    public static boolean mayStartBeRemoved() {
        return numberOfPaths == 0 & getPathStart() != -1;
    }

    public static int getPathStart() {
        return PathStart;
    }

    public static void setPathStart(int start) {
        PathStart = start;
        Controller controller = Main.getController();
        if (start == -1) {
            controller.setDisableRadioButtonsForVisualisation(true);
            controller.disableFromTextField(false);
            GraphView.updateNodesColorByWage();
            controller.TF_From.setText("");
        } else {
            controller.setDisableRadioButtonsForVisualisation(false);
            controller.disableFromTextField(true);
            GraphView.getNodeView(start).selectColor();
            controller.TF_From.setText("" + start);
        }
    }

    public static boolean doesPathExist(int end) {
        for (int element = 0; element < numberOfPaths; element++) {
            if (listElements[element].PathEnd == end)
                return true;
        }
        return false;
    }

    public static boolean canAddNewPath() {
        return numberOfPaths < maxNumberOfPaths;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getNumberOfPaths() {
        return numberOfPaths;
    }

    public Label getL_content() {
        return L_content;
    }

    private static void update() {
        for (int i = 0; i < numberOfPaths; i++) {
            listElements[i].getP_pane().setLayoutY(listElements[i].id * heightOfLabel);
            listElements[i].getL_content().setText(listElements[i].getNewLabel());
        }
    }

    private String getNewLabel() {
        return " " + this.id + ". " + PathStart + "-->" + this.PathEnd;
    }

    public Pane getP_pane() {
        return P_pane;
    }

    public static void Reset() {
        for (int i = numberOfPaths - 1; i >= 0; i--) {
            listElements[i].deleteElement();
        }
        PathStart = -1;
    }
}
