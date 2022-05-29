package graph;

import java.util.Iterator;
import java.util.Queue;

public class Grid implements Iterable<Object> {
    private final Searcher graphSearcher;
    private Node[] nodes;
    private int numberOfRows;
    private int numberOfColumns;

    public Grid() {
        this(0, 0);
    }

    public Grid(int numberOfRows, int numberOfColumns) {
        graphSearcher = new Searcher(this);
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
    }

    public Node getNodeAtIndex(int index) {
        return nodes[index];
    }

    public int getNodeNumAtDirection(int parentNode, Direction direction) throws IncorrectGraphStructureException {
        switch (direction) {
            case NORTH:
                return parentNode - getNumberOfColumns();
            case EAST:
                return parentNode + 1;
            case SOUTH:
                return parentNode + getNumberOfColumns();
            case WEST:
                return parentNode - 1;
            default:
                throw new IncorrectGraphStructureException("Such connection cannot exist.");
        }
    }

    public Queue<Integer> getPath(int source, int destination, boolean bfs) throws IncoherentException {
        return graphSearcher.getPathing(source, destination, bfs);
    }


    public Queue getWeights(int source, int destination, boolean bfs) throws IncoherentException {
        return graphSearcher.getNextWeights(source, destination, bfs);
    }

    public Searcher getGraphSearcher() {
        return graphSearcher;
    }

    public int[] getPreviousNodes(int sourceNode, boolean bfs) throws IncoherentException {
        return graphSearcher.getPreviousNodes(sourceNode, bfs);
    }

    public double[] getWeightsToNodes(int sourceNode, boolean bfs) {
        return graphSearcher.weightsToNodes(sourceNode, bfs);
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {

            private int index;

            @Override
            public boolean hasNext() {
                return index < getNumberOfColumns() * getNumberOfRows();
            }

            @Override
            public Object next() {
                return getNodeAsString(index++);
            }
        };
    }

    private String getNodeAsString(int nodeNumber) {
        StringBuilder result = new StringBuilder("\t");
        for (int i = 0; i < Node.NUMBER_OF_EDGES; i++) {
            Direction direction = Direction.directionFromInteger(i);
            if (getNodeAtIndex(nodeNumber).edgeToDirectionExists(direction))
                try {
                    result.append(getNodeNumAtDirection(nodeNumber, direction))
                            .append(": ")
                            .append(Math.round(10000.0 * getNodeAtIndex(nodeNumber).getWeightOfEdgeToDirection(direction)) / 10000.0)
                            .append("\t");
                } catch (IncorrectGraphStructureException exception) {
                }
        }
        result.append("\n");
        return result.toString();
    }

    public int size() {
        return numberOfColumns * numberOfRows;
    }
}
