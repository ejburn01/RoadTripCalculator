public class BackendGraphNode {

    private String city;
    private double x;
    private double y;
    private String stateName;
    private int population;

    /**
     * Constructor for the GraphNode class.
     */
    public BackendGraphNode(String city, double x, double y, String stateName, int population) {
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

}
