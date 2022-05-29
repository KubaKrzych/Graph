package gui;

import graph.Direction;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeView extends Line {

    public EdgeView(Direction dir, int x, int y, Color color, int length) {
        this.setStartY(y);
        this.setStartX(x);
        this.setEndY(y);
        this.setEndX(x);
        this.setStroke(color);
        this.setVisible(true);
        this.setStrokeWidth(2);
        switch (dir) {
            case NORTH:
                this.setEndY(y - length);
                this.setStartX(x - 1);
                this.setEndX(x - 1);
                break;
            case EAST:
                this.setEndX(x + length);
                this.setStartY(y + 1);
                this.setEndY(y + 1);
                break;
            case SOUTH:
                this.setEndY(y + length);
                this.setStartX(x + 1);
                this.setEndX(x + 1);
                break;
            case WEST:
                this.setEndX(x - length);
                this.setStartY(y - 1);
                this.setEndY(y - 1);
                break;
        }
    }
}
