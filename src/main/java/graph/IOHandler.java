package graph;

import java.io.IOException;

public class IOHandler {

    public static Node[] readGraphFromFile(String path) throws IOException {
        return Reader.readGraphFromFile(path);
    }

    public static void saveGraphToFile(String filename, Grid grid) throws IOException {
        Writer.saveGraphToFile(filename, grid);
    }

}
