package graph;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class TestIOHandler {

    private static Graph graph;

    @Before
    public void setUp(){
        graph = new Graph();
        graph.setNumberOfRows(5);
        graph.setNumberOfColumns(5);
        graph.generateNewGraph(ModeOfGraph.WAGE_RAND);
    }

    @Test
    public void testAndCompareInputAndOutputResults() throws IOException{
        graph.saveGraphToFile(System.getProperty("user.dir") + "/data/testfile.txt");
        Node[] nodes = Reader.readGraphFromFile(System.getProperty("user.dir") + "/data/testfile.txt");
        for(int i = 0; i < nodes.length; i++)
            for (int j = 0; j < Node.NUMBER_OF_EDGES; j++) {
                Direction direction = Direction.directionFromInteger(j);
                assertEquals(nodes[i].getWeightOfEdgeToDirection(direction),
                        graph.getNodeAtIndex(i).getWeightOfEdgeToDirection(direction), 0.0001);
            }
    }

    @AfterClass
    public static void cleanUp() {
        try{
            File file = new File("test/testfile");
            file.delete();
        } catch(Exception exception){
            exception.printStackTrace(); //TODO nie mozna usunac pliku
        }
    }
}
