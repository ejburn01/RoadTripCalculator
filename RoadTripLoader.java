// --== CS400 File Header Information ==--
// Name: Shaun Chander
// Email: sachander@wisc.edu
// Team: BS
// TA: Mohit Loganathan
// Lecturer: Gary Dahl.
// Notes to Grader: N/A

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads data from a JSON file.
 */
public class RoadTripLoader {

    protected static List<IGraphNode> graphNodes;

    /**
     * Constructor for the RoadTripLoader class.
     */
    public RoadTripLoader() {
        graphNodes = new ArrayList<IGraphNode>();
    }

    /**
     * Loads the data from the JSON file.
     *
     * @return a list of GraphNodes
     */
    public List<IGraphNode> loadCities(String filePath) throws FileNotFoundException {

        // Loading the passed in filePath.
        String workingPathToFile = System.getProperty("user.dir").concat(File.separator + filePath);

        File workingFile = new File(workingPathToFile);

        Scanner workingScanner = new Scanner(workingFile);

        // Read the file line by line.
        while (workingScanner.hasNextLine()) {
            // System.out.println(workingScanner.nextLine());
            if (workingScanner.nextLine().trim().equals("{")) {
                // This is the opening of a new JSON object, hence we loop through until the
                // closing "}" is found.

                // Construct a workingLine to hold line values.
                String workingLine = "";

                // Construct a working ArrayList to add values into for later use in
                // constructing the GraphNode.
                List<String> workingValues = new ArrayList<>();

                // Loop through until the closing "}" is found.
                while (true) {

                    // Read the line.
                    workingLine = workingScanner.nextLine().trim();

                    // If the line is equal to "}" then quite the loop
                    if (workingLine.equals("}") || workingLine.equals("},")) {
                        break;
                    }
                    // Use a regular expression to remove the key and leave the value.
                    String[] workingSplit = workingLine.split(":");

                    // Add the value to the working ArrayList.
                    workingValues.add(workingSplit[1].trim().replaceAll("\"", "").replaceAll(",", ""));
                }

                // Create a new GraphNode.
                IGraphNode workingGraphNode = new GraphNode(
                        workingValues.get(0),
                        Double.parseDouble(workingValues.get(2)),
                        Double.parseDouble(workingValues.get(3)),
                        workingValues.get(6),
                        Integer.parseInt(workingValues.get(4)));
                // Add the GraphNode to the list.
                graphNodes.add(workingGraphNode);
            }

        }

        // Close the scanner
        workingScanner.close();

        return graphNodes;
    }

    /**
     * Filters the cities based on the given input.
     *
     * @param citiesToFilterOut a list of cities to filter out.
     * @return a list of cities that are not in the list of cities to filter out.
     */
    public List<IGraphNode> filterCities(List<IGraphNode> citiesToFilterOut) throws IllegalArgumentException {

        // Check if we have graphNodes. If not then throw an exception.
        if (graphNodes == null) {
            throw new IllegalArgumentException("❌ GraphNodes are not loaded.");
        }

        // If the input is null then just return the current graphNodes.
        if (citiesToFilterOut == null) {
            return graphNodes;
        }

        // Create a new list to hold the filtered cities.
        List<IGraphNode> workingFilteredCities = new ArrayList<>();

        // Loop through the graphNodes.
        for (IGraphNode workingGraphNode : graphNodes) {
            // Check if the city is in the list of cities to filter out.
            if (!citiesToFilterOut.contains(workingGraphNode)) {
                // Add the city to the list of filtered cities.
                workingFilteredCities.add(workingGraphNode);
            }
        }

        return workingFilteredCities;
    }

}
