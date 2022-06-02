package gui;

import application.Main;
import graph.Direction;
import graph.Graph;
import graph.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import static graph.Direction.*;
import static java.lang.Math.max;

public class GraphView {
    private static Pane pane;
    private static int numberOfColumns;
    private static int numberOfRows;
    private static final int sizeN = 18;
    private static final int sizeE = 18;
    private static int startX;
    private static int startY;

    private static NodeView[] nodeViews;

    public GraphView(Graph graph) {
        double minEdgeWage = graph.getMinWeight();
        double maxEdgeWage = graph.getMaxWeight();
        numberOfColumns = graph.getNumberOfColumns();
        numberOfRows = graph.getNumberOfRows();
        nodeViews = new NodeView[numberOfRows * numberOfColumns];
        int layoutX, layoutY, id, length = sizeE + sizeN / 2;
        double edgeWage;
        newPane();
        Direction[] directions = {NORTH, EAST, SOUTH, WEST};
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                layoutX = startX + column * sizeN + (column - 1) * sizeE;
                layoutY = startY + row * sizeN + (row - 1) * sizeE;
                id = column + numberOfColumns * row;
                Node node = graph.getNodeAtIndex(id);
                for (int i = 0; i < 4; i++) {
                    if (node.getWeightOfEdgeToDirection(directions[i]) != 0) {
                        edgeWage = node.getWeightOfEdgeToDirection(directions[i]);
                        EdgeView edgeView = new EdgeView(directions[i], layoutX, layoutY, getColorFromScale(maxEdgeWage, minEdgeWage, edgeWage), length);
                        pane.getChildren().add(edgeView);
                    }
                }
                NodeView nodeView = new NodeView(layoutX, layoutY, sizeN / 2, id);
                pane.getChildren().add(nodeView);
                nodeViews[id] = nodeView;
            }
        }
    }

    private static double getMaxWage(double[] Wages) {
        double max = 0;
        for (double wage : Wages) {
            if (wage > max & wage < Math.pow(10, 9))
                max = wage;
        }
        return max;
    }

    public static NodeView getNodeView(int id) {
        return nodeViews[id];
    }

    private void newPane() {
        pane = new Pane();
        pane.setMinSize(400, 400);
        int lengthX, lengthY;
        if (numberOfColumns <= 10)
            lengthX = 400;
        else
            lengthX = 20 + (numberOfColumns) * sizeN + (numberOfColumns - 1) * sizeE + 20;
        if (numberOfRows <= 10)
            lengthY = 400;
        else
            lengthY = 20 + (numberOfRows) * sizeN + (numberOfRows - 1) * sizeE + 20;
        int lengthMAX = max(lengthX, lengthY);
        startX = 40 + (lengthMAX - lengthX) / 2;
        startY = 40 + (lengthMAX - lengthY) / 2;

        pane.setPrefSize(lengthMAX, lengthMAX);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }

    public static Pane getPane() {
        return pane;
    }

    public static void updateNodesColorByWage() {
        for (int nodesId = 0; nodesId < nodeViews.length; nodesId++) {
            nodeViews[nodesId].setColor(Color.GRAY);
            if (ListElement.doesPathExist(nodesId) | nodesId == ListElement.getPathStart())
                nodeViews[nodesId].setColor(Color.WHITE);
        }
    }

    public static void updateNodesColorByWage(double[] Wage) {
        double max = GraphView.getMaxWage(Wage);
        for (int nodesId = 0; nodesId < nodeViews.length; nodesId++) {
            if (Wage[nodesId] > 0) {
                if (ListElement.doesPathExist(nodesId))
                    nodeViews[nodesId].setColor(Color.WHITE);
                else if (nodesId == ListElement.getPathStart() & ListElement.getNumberOfPaths() != 0)
                    nodeViews[nodesId].setColor(Color.WHITE);
                else
                    nodeViews[nodesId].setColor(GraphView.getColorFromScale(max, 0, Wage[nodesId]));
            } else
                nodeViews[nodesId].setDefaultColor();
        }
    }

    public static Color getColorFromScale(double MaxValue, double minValue, double value) {
        if (value < 0 || MaxValue < value || MaxValue < minValue || value < minValue) {
            if (Main.debug)
                System.out.println("Given value is out of scope. min value = " + minValue + ", max value = " + MaxValue + ", value given = " + value);
            return Color.WHITE;
        }
        if (minValue == MaxValue) {
            return Color.WHITE;
        }
        int standardizedValue = (int) Math.round((value - minValue) * 1275 / (MaxValue - minValue));
        if ((0 <= standardizedValue) && (standardizedValue < 255)) {
            return Color.rgb((255 - standardizedValue), 255, 0);
        }
        if ((255 <= standardizedValue) && (standardizedValue < 2 * 255)) {
            return Color.rgb(0, 255, (standardizedValue - 255));
        }
        if ((2 * 255 <= standardizedValue) && (standardizedValue < 3 * 255)) {
            return Color.rgb(0, 255 * 3 - standardizedValue, 255);
        }
        if ((3 * 255 <= standardizedValue) && (standardizedValue < 4 * 255)) {
            return Color.rgb(standardizedValue - 255 * 3, 0, 255);
        }
        if ((4 * 255 <= standardizedValue) && (standardizedValue <= 5 * 255)) {
            return Color.rgb(255, 0, 255 * 5 - standardizedValue);
        }
        return null;
    }
}