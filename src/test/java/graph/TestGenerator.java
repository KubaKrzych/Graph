package graph;

import org.junit.*;

import static org.junit.Assert.*;

public class TestGenerator {

    private Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
        graph.setNumberOfColumns(3);
        graph.setNumberOfRows(3);
        graph.setMinWeightOfEdge(0.0001);
        graph.setMaxWeightOfEdge(1);
        graph.generateNewGraph(ModeOfGraph.WAGE_RAND);
    }

    @Test
    public void testAllRandomGridGenerator() {
        graph.generateNewGraph(ModeOfGraph.ALL_RAND);
    }

    @Test
    public void testWageRandomGridGenerator() {
        graph.generateNewGraph(ModeOfGraph.WAGE_RAND);
    }

    @Test
    public void testEdgeRandomGridGenerator() {
        graph.generateNewGraph(ModeOfGraph.EDGE_RAND);
    }

    @Test
    public void testAddingEdgeBetweenTwoNodes() {
        Graph.GRAPH_GENERATOR.addEdgeFromNodeNumToDirection(4, Direction.EAST);
        Graph.GRAPH_GENERATOR.addEdgeFromNodeNumToDirection(4, Direction.WEST);
        Graph.GRAPH_GENERATOR.addEdgeFromNodeNumToDirection(4, Direction.SOUTH);
        Graph.GRAPH_GENERATOR.addEdgeFromNodeNumToDirection(4, Direction.NORTH);
        assertEquals(0.5, Graph.GRAPH_GENERATOR.getNodeAtIndex(4).getWeightOfEdgeToDirection(Direction.NORTH),
                0.5);
        assertEquals(0.5, Graph.GRAPH_GENERATOR.getNodeAtIndex(4).getWeightOfEdgeToDirection(Direction.WEST),
                0.5);
        assertEquals(0.5, Graph.GRAPH_GENERATOR.getNodeAtIndex(4).getWeightOfEdgeToDirection(Direction.EAST),
                0.5);
        assertEquals(0.5, Graph.GRAPH_GENERATOR.getNodeAtIndex(4).getWeightOfEdgeToDirection(Direction.SOUTH),
                0.5);
    }


}
