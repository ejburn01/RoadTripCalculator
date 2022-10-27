// --== CS400 File Header Information ==--
// Name: Shaun Chander
// Email: sachander@wisc.edu
// Team: BS
// TA: Mohit Loganathan
// Lecturer: Gary Dahl.
// Notes to Grader: N/A

/**
 * Represents a node in a connected graph.
 */
public class GraphNode implements IGraphNode {

    private String city;
    private double x; //longitude
    private double y; //latitude
    private String stateName;
    private int population;

    /**
     * Constructor for the GraphNode class.
     */
    public GraphNode(String city, double y, double x, String stateName, int population) {
        this.city = city;
        this.x = x;
        this.y = y;
        this.stateName = stateName;
        this.population = population;
    }

    /**
     * Returns the city name.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Returns the x value.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y value.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Returns the the name of the state.
     */
    public String getStateName() {
        return this.stateName;
    }

    /**
     * Returns the population of the state.
     */
    public int getPopulation() {
        return this.population;
    }

    /**
     * Returns a string representation of the GraphNode.
     */
    @Override
    public String toString() {
        return "{ " + this.city + ", " + this.x + ", " + this.y + ", " + this.stateName + ", " + this.population + " }";
    }

    /**
     * Compares this GraphNode to another GraphNode.
     */
    public boolean equals(IGraphNode other) {
        return this.city.equals(other.getCity()) && this.x == other.getX() && this.y == other.getY()
                && this.stateName.equals(other.getStateName()) && this.population == other.getPopulation();
    }
}
