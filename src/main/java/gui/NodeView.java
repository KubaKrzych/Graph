package gui;

import application.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NodeView extends Circle {
    private boolean hidden = false;
    private int id;
    private Color color;
    private ListElement listElement;

    public NodeView(int x, int y, int r, int id) {
        this.id = id;
        this.color = Color.GRAY;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setRadius(r);
        this.setFill(Color.GRAY);
        this.setOnMouseClicked(mouseEvent -> this.click());
        this.setVisible(true);
    }

    public void setColor(Color color) {
        this.color = color;
        this.setFill(color);
    }

    public void changeColor() {
        this.setFill(Color.WHITE);
    }

    public void setListElement(ListElement listElement) {
        this.listElement = listElement;
    }

    public void resetColor() {
        this.setFill(color);
    }

    public void setDefaultColor() {
        this.setFill(Color.GRAY);
    }

    public void click() {
        Controller controller = Main.getController();
        controller.printMessage(0);
        if (ListElement.getPathStart() == this.id) {
            if (ListElement.getNumberOfPaths() == 0) {
                this.resetColor();
                ListElement.setPathStart(-1);
                controller.disableFromTextField(false);
                controller.setDisableRadioButtonsForVisualisation(true);
                GraphView.updateNodesColorByWage();
            }
        } else if (ListElement.getPathStart() == -1) {

            ListElement.setPathStart(this.id);
            this.changeColor();
            controller.disableFromTextField(true);
            controller.TF_From.setText("" + this.id);
            controller.setDisableRadioButtonsForVisualisation(false);

        } else if (ListElement.doesPathExist(this.id)) {

            this.listElement.deleteElement();

        } else if (ListElement.canAddNewPath()) {

            this.listElement = new ListElement(ListElement.getPathStart(), this.id);
        }
    }
}
