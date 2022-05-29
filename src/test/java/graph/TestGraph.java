package graph;

import org.junit.*;
import static org.junit.Assert.*;

public class TestGraph {

    private static Graph graph;

    @BeforeClass
    public static void setGraph() {
        graph = new Graph();
        graph.setNumberOfColumns(5);
        graph.setNumberOfRows(10);
        graph.setMinWeightOfEdge(5);
        graph.setMaxWeightOfEdge(10);
        graph.generateNewGraph(ModeOfGraph.ALL_RAND);
    }

    @Test
    public void setValuesForBoundsShouldWorkCorrectly(){
        double delta = 0.0001;
        assertEquals(5, Graph.GRAPH_GENERATOR.getMinWeight(), delta);
        assertEquals(10, Graph.GRAPH_GENERATOR.getMaxWeight(), delta);
    }

    @Test
    public void setNumberOfRowsAndColsShouldWorkCorrectly(){
        assertEquals(5, Graph.GRAPH_GENERATOR.getNumberOfColumns());
        assertEquals(10, Graph.GRAPH_GENERATOR.getNumberOfRows());
    }
}
