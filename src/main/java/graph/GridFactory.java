package graph;

public abstract class GridFactory {

    private static final Generator GRAPH_GENERATOR = Graph.GRAPH_GENERATOR;

    public static Node[] createGraph(ModeOfGraph modeOfGraph) {

        switch (modeOfGraph) {
            case ALL_RAND:
                return GRAPH_GENERATOR.generateAllRandomGrid();
            case EDGE_RAND:
                return GRAPH_GENERATOR.generateEdgeRandomGrid();
            case WAGE_RAND:
                return GRAPH_GENERATOR.generateWageRandomGrid();
            default:
                return GRAPH_GENERATOR.generateBlankGrid();
        }
    }

    private static Generator getGenerator() {
        return GRAPH_GENERATOR;
    }
}
