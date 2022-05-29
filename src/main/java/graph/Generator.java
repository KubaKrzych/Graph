package graph;

import java.util.Random;

public class Generator extends Grid implements Iterable<Object> {

    private static final double CHANCE_OF_EDGE_TO_EXIST = 50;
    private static final Random random = new Random();

    private final Validator validator = new Validator(this);
    private double minWeight = 0.0001;
    private double maxWeight = 1;


    public Node[] generateBlankGrid() {
        setNodes(new Node[size()]);
        for (int i = 0; i < size(); i++)
            getNodes()[i] = new Node();
        return getNodes();
    }


    public Node[] generateAllRandomGrid() {
        if(shouldGenerateNewGrid())
            generateBlankGrid();
        for (int nodeNumber = 0; nodeNumber < size(); nodeNumber++)
            for (int i = Direction.NORTH.getValue(); i <= Direction.WEST.getValue(); i++) {
                Direction direction = Direction.directionFromInteger(i);
                if (validator.edgeCouldExistFromNodeToDirection(nodeNumber, direction) && randomChanceOfEdgeExist())
                    addEdgeFromNodeNumToDirection(nodeNumber, direction);
            }
        return getNodes();
    }

    public Node[] generateEdgeRandomGrid() {
        boolean coherent = true;
        do {
            generateAllRandomGrid();
            coherent = getGraphSearcher().isCoherent(0);
        } while (!coherent);
        return getNodes();
    }

    public Node[] generateWageRandomGrid() {
        if (shouldGenerateNewGrid())
            generateBlankGrid();
        for (int nodeNumber = 0; nodeNumber < getNumberOfColumns() * getNumberOfRows(); nodeNumber++)
            for (int i = Direction.NORTH.getValue(); i <= Direction.WEST.getValue(); i++) {
                Direction direction = Direction.directionFromInteger(i);
                if (validator.edgeCouldExistFromNodeToDirection(nodeNumber, direction))
                    addEdgeFromNodeNumToDirection(nodeNumber, direction);
            }
        return getNodes();
    }

    public void addWeightedEdgeBetweenTwoNodes(int parentNode, int neighbourNode, double weight) throws IncorrectGraphStructureException {
        Direction direction = validator.directionFromNodeToNode(parentNode, neighbourNode);
        getNodeAtIndex(parentNode).setWeightOfEdgeToDirection(weight, direction);
    }

    private double getRandomDouble() {
        return minWeight + (maxWeight - minWeight) * random.nextDouble();
    }

    private boolean randomChanceOfEdgeExist() {
        return random.nextInt(100) < CHANCE_OF_EDGE_TO_EXIST;
    }

    public void addEdgeFromNodeNumToDirection(int parentNode, Direction direction) {
        try {
            getNodeAtIndex(parentNode).setWeightOfEdgeToDirection(getRandomDouble(), direction);
            int neighbourNodeNumber = getNodeNumAtDirection(parentNode, direction);
            Direction oppositeDirection = Direction.getOppositeDirection(direction);
            getNodeAtIndex(neighbourNodeNumber).setWeightOfEdgeToDirection(getRandomDouble(), oppositeDirection);
        } catch (Exception exception) {
        }
    }

    public void setMinWeight(double minWeight) {
        this.minWeight = minWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    private boolean shouldGenerateNewGrid() {
        return getNodes() == null || getNodes().length != size();
    }

    private void resetGrid(){
        for(int i = 0; i < size(); i++)
            this.getNodeAtIndex(i).resetNode();
    }
}
