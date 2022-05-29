package graph;

public enum Direction {

    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3);

    private final int value;

    Direction(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static Direction directionFromInteger(int direction)  throws IncorrectDirectionException {
        switch(direction){
            case 0:
                return NORTH;
            case 1:
                return EAST;
            case 2:
                return SOUTH;
            case 3:
                return WEST;
            default:
                throw new IncorrectDirectionException("Nie istnieje podany kierunek.");
        }
    }
    public static Direction getOppositeDirection(Direction direction) throws IncoherentException{
        switch (direction){
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            default:
                throw new IncoherentException("Nie istnieje podany kierunek.");
        }
    }

    public static Direction[] getAllDirections(){
        return new Direction[]{NORTH, EAST, SOUTH, WEST};
    }
}
