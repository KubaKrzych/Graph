package graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    public static void saveGraphToFile(String filename, Grid grid) throws IOException {
        BufferedWriter writer;
        if (filename.endsWith(".txt"))
            writer = new BufferedWriter(new FileWriter(filename));
        else
            writer = new BufferedWriter(new FileWriter(filename + ".txt"));
        writer.write("" + Graph.GRAPH_GENERATOR.getNumberOfRows() + " " + Graph.GRAPH_GENERATOR.getNumberOfColumns() + "\n");
        for (Object obj : grid) {
            writer.write(obj.toString());
        }
        writer.close();
    }
}
