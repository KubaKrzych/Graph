package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    public static Node[] readGraphFromFile(String path) throws IOException {
        int lineNumber = 0;
        double minWeight = Double.MAX_VALUE;
        double maxWeight = 0.0;
        double weight;
        int node;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        String line = bufferedReader.readLine();
        String[] splittedWords = line.split("\\s");
        Generator generator = new Generator();

        if (splittedWords.length != 2)
            throw new IOException("File's first line must contain number of rows x number of columns.");
        try {
            setGenerator(generator, Integer.parseInt(splittedWords[0]), Integer.parseInt(splittedWords[1]));
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim() + "\n";
                line = line.replaceAll(":", "");
                splittedWords = line.split("\\s+");
                if (splittedWords.length % 2 != 0)
                    throw new IOException("Incorrect file structure. Line nr.:" + (lineNumber + 1));
                for (int i = 0; i < splittedWords.length; i += 2) {
                    node = Integer.parseInt(splittedWords[i]);
                    weight = Math.round(Double.parseDouble(splittedWords[i + 1]) * 10000.0) / 10000.0;
                    generator.addWeightedEdgeBetweenTwoNodes(lineNumber, node, weight);
                    if (weight > maxWeight)
                        maxWeight = weight;
                    else if (weight < minWeight)
                        minWeight = weight;
                    else if (weight < 0)
                        throw new IOException("Edge weight must not be negative.");
                }
                lineNumber++;
            }
        } catch (NumberFormatException | IncorrectGraphStructureException | ArrayIndexOutOfBoundsException exception) {
            throw new IOException("Incorrect file structure. Line nr.:" + (lineNumber + 1));
        } finally {
            bufferedReader.close();
        }
        setGraphsGenerator(generator, minWeight, maxWeight);
        return generator.getNodes();
    }

    private static void setGenerator(Generator generator, int numberOfRows, int numberOfColumns) throws IOException {
        if (numberOfRows > 300 || numberOfColumns > 300 || numberOfColumns < 0 || numberOfRows < 0)
            throw new IOException("Niepoprawna liczba kolumn, lub liczba wierszy.");
        generator.setNumberOfRows(numberOfRows);
        generator.setNumberOfColumns(numberOfColumns);
        generator.generateBlankGrid();
    }

    private static void setGraphsGenerator(Generator generator, Double minWeight, double maxWeight) {
        Graph.GRAPH_GENERATOR.setNodes(generator.getNodes());
        Graph.GRAPH_GENERATOR.setMaxWeight(maxWeight);
        Graph.GRAPH_GENERATOR.setMinWeight(minWeight);
        Graph.GRAPH_GENERATOR.setNumberOfRows(generator.getNumberOfRows());
        Graph.GRAPH_GENERATOR.setNumberOfColumns(generator.getNumberOfColumns());
    }
}
