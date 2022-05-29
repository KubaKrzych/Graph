package graph;


import org.junit.*;

import static org.junit.Assert.*;

public class TestNode {
    private Node node;
    private double[] weightsOfEdges;
    private final double delta = 0.0001;


    @Before
    public void setNode() {
        weightsOfEdges = new double[]{1.2, 2.235, 0, 4.2534};
        node = new Node(weightsOfEdges[0], weightsOfEdges[1], weightsOfEdges[2], weightsOfEdges[3]);
    }

    @Test
    public void testIfEdgesExist() {
        assertTrue(node.edgeToDirectionExists(Direction.NORTH));
        assertTrue(node.edgeToDirectionExists(Direction.EAST));
        assertFalse(node.edgeToDirectionExists(Direction.SOUTH));
        assertTrue(node.edgeToDirectionExists(Direction.WEST));
    }

    @Test
    public void testIfWeightsAreCorrectlySet() {
        assertEquals(weightsOfEdges[Direction.NORTH.getValue()], node.getWeightOfEdgeToDirection(Direction.NORTH), delta);
        assertEquals(weightsOfEdges[Direction.EAST.getValue()], node.getWeightOfEdgeToDirection(Direction.EAST), delta);
        assertEquals(weightsOfEdges[Direction.WEST.getValue()], node.getWeightOfEdgeToDirection(Direction.WEST), delta);
        assertEquals(weightsOfEdges[Direction.SOUTH.getValue()], node.getWeightOfEdgeToDirection(Direction.SOUTH), delta);
    }

    @Test
    public void testUpdatingWeightOfAnEdge() {
        double weight = 3.6;
        Direction direction = Direction.NORTH;
        node.setWeightOfEdgeToDirection(weight, direction);

        assertEquals(node.getWeightOfEdgeToDirection(direction), weight, delta);
    }
}
