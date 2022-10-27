// Alesandra Brittis-Tannenbaum
// CSL Username: alesandra
// Email: brittistanne@wisc.edu 
// Lecture: 002 
// Team: BS Red 

import java.util.List;
import java.util.ArrayList;
//import java.util.PriorityQueue;

public class RoadTripBackend  {

    // Variables
    protected GraphADT<IGraphNode> graph; // A graph with user selected cities
    protected int size;

    // Constructor that creates a new graph
    public RoadTripBackend() {
        size = 0;
        graph = new GraphADT<IGraphNode>();
    }

    /**
     * Add a city to the backend database
     */
    public void addCity(IGraphNode city) {
        graph.insertVertex(city);
        size++;
    }

    /**
     * Create a list of the optimal order to visit the cities
     * 
     * @param cities The cities that the user entered
     * @return an ordered list of the cities that the user should visit
     */
    public List<IGraphNode> cityList(List<IGraphNode> cities) {
        graph.insertVertex(cities.get(0)); // add the starting city to the graph
        IGraphNode currCity;
        for (int i = 0; i < cities.size(); i++) {
            currCity = cities.get(i);
            for (int j = 0; j < cities.size(); j++) {
                if (currCity.equals(cities.get(j))) { // if the city is already in the graph
                    continue;
                } else {
                    graph.insertVertex(cities.get(j)); // add each city as a vertex
                    double tempCost = calculateDistance(cities.get(i), cities.get(j)); // get the distances between

                    graph.insertEdge(cities.get(i), cities.get(j), tempCost); // add the distances between cities to
                                                                              // the graph

                }
            }
        }
        List<IGraphNode> finalList = graph.shortestPath(cities);

        // Clear the graph for the next iteration.
        graph = new GraphADT<>();

        size = 0;

        // Return the ordered nodes to the user.
        return finalList;
    }

    /**
     * Returns the size the cities in the dijkstra
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Calculate the distance between two cities
     * 
     * @param city1 the first city
     * @param city2 the second city
     * @return the rounded distance between the two cities
     */
    public double calculateDistance(IGraphNode city1, IGraphNode city2) {
        double distance = 0;
	double long1 = city1.getX();
	double lat1 = city1.getY();
	double long2 = city2.getX();
	double lat2 = city2.getY();
	lat1 = Math.toRadians(lat1);

        lat2 = Math.toRadians(lat2);

        long1 = Math.toRadians(long1);

        long2 = Math.toRadians(long2);



        double calc1 = Math.pow(   (Math.sin((lat2 - lat1)/2)), 2.0);

        double calc2 = Math.cos(lat1) *

                Math.cos(lat2) * Math.pow(   (Math.sin((long2 - long1)/2)) , 2.0)    ;

        double calc3 = Math.sqrt(calc1 + calc2);

        distance = 2 * (3958.8) * Math.asin(calc3);

        return distance;


       // double xVal = Math.abs(city1.getX() - city2.getX());
       // double yVal = Math.abs(city1.getY() - city2.getY());
       // distance = Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal, 2));
       // distance = Math.round(distance);
       // return distance;
    }

}
