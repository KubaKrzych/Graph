package graph;

import java.util.Arrays;

public class Node {
    public static final int NUMBER_OF_EDGES = 4;

    private final double[] weightsOfEdgesToDirection;

    public Node() {
        this(0, 0, 0, 0);
    }

    public Node(double weightOfNorthEdge, double weightOfEastEdge, double weightOfSouthEdge, double weightOfWestEdge) {
        weightsOfEdgesToDirection = new double[]{
                weightOfNorthEdge, weightOfEastEdge, weightOfSouthEdge, weightOfWestEdge};
    }

    public double getWeightOfEdgeToDirection(Direction direction) {
        return weightsOfEdgesToDirection[direction.ordinal()];
    }

    public boolean edgeToDirectionExists(Direction direction) {
        return weightsOfEdgesToDirection[direction.getValue()] > 0;
    }

    public void setWeightOfEdgeToDirection(double weightOfEdge, Direction direction) {
        weightsOfEdgesToDirection[direction.getValue()] = weightOfEdge;
    }

    public void resetNode() {
        Arrays.fill(weightsOfEdgesToDirection, 0.0);
    }


    @Override
    public String toString() {
        return Arrays.toString(weightsOfEdgesToDirection);
    }
}
