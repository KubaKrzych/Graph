package graph;

import static graph.Direction.*;
public class Validator {
    private final Generator generator;

    public Validator(Generator generator) {
        this.generator = generator;
    }

    public boolean edgeCouldExistFromNodeToDirection(int numberOfNode, Direction direction) {
        return !nodeIsLastInRowAndNeighbourIsTheInNextRow(numberOfNode, direction) &&
                !nodeIsInFirstColumnAndNeighbourIsInRowBefore(numberOfNode, direction) &&
                !nodeIsInFirstRowAndNeighbourIsInTheRowBefore(numberOfNode, direction) &&
                !nodeIsInLastRowAndNeighbourIsInTheNextRow(numberOfNode, direction);
    }

    private boolean nodeIsLastInRowAndNeighbourIsTheInNextRow(int numberOfNode, Direction direction) {
        return numberOfNode % generator.getNumberOfColumns() == generator.getNumberOfColumns() - 1
                && direction == EAST;
    }

    private boolean nodeIsInFirstColumnAndNeighbourIsInRowBefore(int numberOfNode, Direction direction) {
        return numberOfNode % generator.getNumberOfColumns() == 0 && direction == Direction.WEST;
    }

    private boolean nodeIsInLastRowAndNeighbourIsInTheNextRow(int numberOfNode, Direction direction) {
        return numberOfNode >= generator.getNumberOfColumns() * (generator.getNumberOfRows() - 1)
                && direction == SOUTH;
    }

    private boolean nodeIsInFirstRowAndNeighbourIsInTheRowBefore(int numberOfNode, Direction direction) {
        return numberOfNode < generator.getNumberOfColumns() && direction == NORTH;
    }

    public Direction directionFromNodeToNode(int fromNode, int toNode) throws IncorrectGraphStructureException {
        if (fromNode == toNode + 1)
            return WEST;
        else if (fromNode == toNode - 1)
            return EAST;
        else if (fromNode == toNode + generator.getNumberOfColumns())
            return NORTH;
        else if (fromNode == toNode - generator.getNumberOfColumns())
            return SOUTH;
        throw new IncorrectGraphStructureException("Nie istnieje droga między parą wierzchołków o numerach ("
                + fromNode + ", " + toNode + ").");
    }


}
