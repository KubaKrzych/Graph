package graph;

import java.io.IOException;


public class Graph extends Grid implements Iterable<Object> {
    public static final Generator GRAPH_GENERATOR = new Generator();

    public Graph() {
        super(0, 0);
        setNodes(GridFactory.createGraph(ModeOfGraph.BLANK));
    }

    public void generateNewGraph(ModeOfGraph modeOfGraph) {
        setNodes(GridFactory.createGraph(modeOfGraph));
    }

    public double getMaxWeight() {
        return GRAPH_GENERATOR.getMaxWeight();
    }

    public double getMinWeight() {
        return GRAPH_GENERATOR.getMinWeight();
    }

    @Override
    public void setNumberOfColumns(int numberOfColumns) {
        GRAPH_GENERATOR.setNumberOfColumns(numberOfColumns);
    }

    @Override
    public void setNumberOfRows(int numberOfRows) {
        GRAPH_GENERATOR.setNumberOfRows(numberOfRows);
    }

    @Override
    public int getNumberOfRows() {
        return GRAPH_GENERATOR.getNumberOfRows();
    }

    @Override
    public int getNumberOfColumns() {
        return GRAPH_GENERATOR.getNumberOfColumns();
    }

    public void setMinWeightOfEdge(double minWeightOfEdge) {
        GRAPH_GENERATOR.setMinWeight(minWeightOfEdge);
    }

    public void setMaxWeightOfEdge(double maxWeightOfEdge) {
        GRAPH_GENERATOR.setMaxWeight(maxWeightOfEdge);
    }

    public void saveGraphToFile(String path) throws IOException {
        IOHandler.saveGraphToFile(path, this);
    }

    public void readGraphFromFile(String path) throws IOException {
        setNodes(IOHandler.readGraphFromFile(path));
    }

    @Override
    public int size() {
        return GRAPH_GENERATOR.size();
    }
}
