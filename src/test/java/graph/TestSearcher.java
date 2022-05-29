package graph;

import static org.junit.Assert.*;

import org.junit.*;

import java.util.Queue;

public class TestSearcher {

    private static Searcher searcher;
    private static Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
        searcher = new Searcher(graph);
        graph.setNumberOfColumns(4);
        graph.setNumberOfRows(4);
        graph.setMinWeightOfEdge(10);
        graph.setMaxWeightOfEdge(1000);
        graph.generateNewGraph(ModeOfGraph.EDGE_RAND);
    }

    @Test
    public void testPathing() throws IncoherentException {
        Queue<Integer> path = searcher.getPathing(0, 13, false);
        assertNotNull(path);
        while (!path.isEmpty())
            assertNotNull(path.remove());
    }

    @Test
    public void testGetWeights() throws IncoherentException {
        Queue<Double> weights = searcher.getNextWeights(0, 13, false);
        assertNotNull(weights);
        while (!weights.isEmpty()) {
            assertNotNull(weights.remove());
        }
    }

    @Test
    public void testGetPreviousNodes() throws IncoherentException {
        int sourceNode = 0;

        boolean bfsSearch = false;
        int[] previousNodes = graph.getPreviousNodes(sourceNode, bfsSearch);
        for (int i = 0; i < graph.size(); i++) {
            if (i != sourceNode)
                assertNotEquals(-1, previousNodes[i]);
        }

        bfsSearch = true;
        previousNodes = graph.getPreviousNodes(sourceNode, bfsSearch);
        for (int i = 0; i < graph.size(); i++) {
            if (i != sourceNode)
                assertNotEquals(-1, previousNodes[i]);
        }
    }

    @Test
    public void testWeightsToNodes() throws IncoherentException {
        int sourceNode = 0;

        boolean bfsSearch = false;
        double[] weightsToNodes = graph.getWeightsToNodes(sourceNode, bfsSearch);
        for (int i = 0; i < graph.size(); i++) {
            if (i != sourceNode)
                assertNotEquals(Double.MAX_VALUE, weightsToNodes[i]);
        }

        bfsSearch = true;
        weightsToNodes = graph.getWeightsToNodes(sourceNode, bfsSearch);
        for (int i = 0; i < graph.size(); i++) {
            if (i != sourceNode)
                assertNotEquals(Double.MAX_VALUE, weightsToNodes[i]);
        }
    }

    @Test
    public void testBfsPathing() throws IncoherentException {
        Queue<Integer> path = searcher.getPathing(0, 15, true);
        assertNotNull(path);
        while (!path.isEmpty())
            assertNotNull(path.remove());
    }

    @Test
    public void testBfsWeights() throws IncoherentException {
        Queue<Double> weights = searcher.getNextWeights(0, 15, true);
        assertNotNull(weights);
        while (!weights.isEmpty())
            assertNotNull(weights.remove());
    }
}
