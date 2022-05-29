package graph;

import org.junit.*;
import static org.junit.Assert.*;

public class TestValidator {

    private static Validator validator;
    private static Graph graph;

    @BeforeClass
    public static void setUp(){
        graph = new Graph();
        graph.setNumberOfColumns(3);
        graph.setNumberOfRows(3);
        graph.generateNewGraph(ModeOfGraph.ALL_RAND);
        validator = new Validator(Graph.GRAPH_GENERATOR);
    }

    @Test
    public void testDirectionBetweenTwoNodeNumbers() throws IncorrectGraphStructureException{
        assertEquals(Direction.EAST, validator.directionFromNodeToNode(4, 5));
        assertEquals(Direction.SOUTH, validator.directionFromNodeToNode(4, 7));
        assertEquals(Direction.NORTH, validator.directionFromNodeToNode(4, 1));
        assertEquals(Direction.WEST, validator.directionFromNodeToNode(4, 3));
    }

    @Test
    public void testForRulesForEdgeExistance(){
        assertFalse(validator.edgeCouldExistFromNodeToDirection(0, Direction.NORTH));
        assertFalse(validator.edgeCouldExistFromNodeToDirection(0, Direction.WEST));
        assertFalse(validator.edgeCouldExistFromNodeToDirection(8, Direction.EAST));
        assertFalse(validator.edgeCouldExistFromNodeToDirection(8, Direction.SOUTH));
        assertTrue(validator.edgeCouldExistFromNodeToDirection(4, Direction.NORTH));
        assertTrue(validator.edgeCouldExistFromNodeToDirection(4, Direction.EAST));
        assertTrue(validator.edgeCouldExistFromNodeToDirection(4, Direction.SOUTH));
        assertTrue(validator.edgeCouldExistFromNodeToDirection(4, Direction.WEST));
    }

    @AfterClass
    public static void cleanup(){
        graph = null;
        validator = null;
    }

}
