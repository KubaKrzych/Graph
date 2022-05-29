package graph;

import java.util.*;

public class Searcher {
    private final Grid grid;
    private PriorityQueue<Integer> priorityQueue;
    private boolean[] visited;
    private boolean[] preVisited;
    private int[] previouslyVisitedNodeNumber;
    private double[] weightToNodeFromSource;
    private int numberOfNodesToVisitLeft;

    public Searcher(Grid grid) {
        this.grid = grid;
    }

    private void setAndResetFields() {
        if (priorityQueue == null) {
            priorityQueue = new PriorityQueue<>(grid.size(), new WeightComparator());
        } else {
            Arrays.fill(visited, false);
            Arrays.fill(preVisited, false);
        }
        if (weightToNodeFromSource == null || grid.size() != weightToNodeFromSource.length) {
            weightToNodeFromSource = new double[grid.size()];
            previouslyVisitedNodeNumber = new int[grid.size()];
            visited = new boolean[grid.size()];
            preVisited = new boolean[grid.size()];
        }
        if (!priorityQueue.isEmpty()) {
            priorityQueue.clear();
        }
        Arrays.fill(weightToNodeFromSource, 0.0);
        Arrays.fill(previouslyVisitedNodeNumber, -1);
        numberOfNodesToVisitLeft = grid.size();
    }

    private void startPathingFromSource(int sourceNode, boolean bfs) {
        setAndResetFields();
        weightToNodeFromSource[sourceNode] = 0;
        priorityQueue.add(sourceNode);
        do {
            int currentNode = priorityQueue.remove();
            numberOfNodesToVisitLeft--;
            visited[currentNode] = true;
            updatePreviousNodeAndWeightToNeighbours(currentNode, bfs);
            addNeighboursToQueue(currentNode);
        } while (!priorityQueue.isEmpty());
    }

    private void addNeighboursToQueue(int nodeNumber) {
        for (Direction direction : Direction.values()) {
            if (grid.getNodeAtIndex(nodeNumber).edgeToDirectionExists(direction)) {
                try {
                    int neighbour = grid.getNodeNumAtDirection(nodeNumber, direction);
                    if (!visited[neighbour] && !preVisited[neighbour]) {
                        priorityQueue.add(neighbour);
                        preVisited[neighbour] = true;
                    }
                } catch (IncorrectGraphStructureException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public double[] weightsToNodes(int sourceNode, boolean bfs) {
        startPathingFromSource(sourceNode, bfs);
        return weightToNodeFromSource;
    }

    public int[] getPreviousNodes(int sourceNode, boolean bfs) throws IncoherentException {
        startPathingFromSource(sourceNode, bfs);
        return previouslyVisitedNodeNumber;
    }

    public boolean isCoherent(int sourceNode) {
        startPathingFromSource(sourceNode, true);
        return numberOfNodesToVisitLeft == 0;
    }

    private void updatePreviousNodeAndWeightToNeighbours(int nodeNumber, boolean bfs) {
        for (int i = 0; i < Node.NUMBER_OF_EDGES; i++) {
            Direction direction = Direction.directionFromInteger(i);
            if (shouldUpdateWeightAndPreviousNode(nodeNumber, direction)) {
                updateBothWeightToNodeAndPreviousNodeNumber(nodeNumber, direction, bfs);
            }
        }
    }

    private boolean shouldUpdateWeightAndPreviousNode(int parentNode, Direction direction) {
        return nodeAtDirectionIsUnvisitedAndEdgeToThatNodeExist(parentNode, direction) &&
                weightFromSourceThruCurrentNodeIsSmaller(parentNode, direction);
    }

    private boolean nodeAtDirectionIsUnvisitedAndEdgeToThatNodeExist(int parentNode, Direction direction) {
        try {
            return grid.getNodeAtIndex(parentNode).edgeToDirectionExists(direction)
                    && !visited[(grid.getNodeNumAtDirection(parentNode, direction))];
        } catch (Exception exception) {
            return false;
        }
    }

    private boolean weightFromSourceThruCurrentNodeIsSmaller(int parentNode, Direction direction) {
        try {
            double currentWeightThruParentNode = weightToNodeFromSource[parentNode]
                    + grid.getNodeAtIndex(parentNode).getWeightOfEdgeToDirection(direction);
            int neighbour = grid.getNodeNumAtDirection(parentNode, direction);
            return currentWeightThruParentNode <= currentWeightThruParentNode + weightToNodeFromSource[neighbour];
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


    private void updateBothWeightToNodeAndPreviousNodeNumber(int parentNode, Direction direction, boolean bfs) {
        try {
            int neighbour = grid.getNodeNumAtDirection(parentNode, direction);
            if (bfs)
                weightToNodeFromSource[neighbour] = weightToNodeFromSource[parentNode] + 1;
            else
                weightToNodeFromSource[neighbour] = weightToNodeFromSource[parentNode] +
                        grid.getNodeAtIndex(parentNode).getWeightOfEdgeToDirection(direction);
            previouslyVisitedNodeNumber[grid.getNodeNumAtDirection(parentNode, direction)] = parentNode;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private class WeightComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return Double.compare(weightToNodeFromSource[o1], weightToNodeFromSource[o2]);
        }
    }

    public Queue<Integer> getPathing(int sourceNode, int destinationNode, boolean bfs) throws IncoherentException {
        startPathingFromSource(sourceNode, bfs);
        Queue<Integer> path = new LinkedList<>();
        int currentNode = destinationNode;
        while (currentNode != sourceNode) {
            try {
                path.add(currentNode);
                currentNode = previouslyVisitedNodeNumber[currentNode];
            } catch (ArrayIndexOutOfBoundsException exception) {
                throw new IncoherentException("Graf jest niespójny.");
            }
        }
        path.add(sourceNode);
        return path;
    }

    //TODO jak scalic te metody, zachowujac ich dzialanie?

    public Queue<Double> getNextWeights(int sourceNode, int destinationNode, boolean bfs) throws IncoherentException {
        startPathingFromSource(sourceNode, bfs);
        Queue<Double> path = new LinkedList<>();
        int currentNode = destinationNode;
        while (currentNode != sourceNode) {
            try {
                path.add(weightToNodeFromSource[currentNode]);
                currentNode = previouslyVisitedNodeNumber[currentNode];
            } catch (ArrayIndexOutOfBoundsException exception) {
                throw new IncoherentException("Graf jest niespójny.");
            }
        }
        return path;
    }
}
