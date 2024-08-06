import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 * @author baranonder , Student ID: 2022400111
 * @version 1.0
 * Turkey Navigaton : This code finds the shortest path between two cities and visualizes it.
 * It reads city coordinates and connections from given files,
 * then uses the Dijkstra algorithm to find the shortest path between the specified start and end cities.
 * Finally, it visualizes the path and calculates the total distance.
 */

public class BaranOnder {
    //This method reads city coordinates and connections from files, creates City objects,
    // establishes connections between cities, and returns a list of City objects.
    public static List<City> readCitiesInfoFromFile(String cityFileName, String connectionFileName) {
        List<City> cities = new ArrayList<>(); // Creating a list of cities

        try (BufferedReader cityTxtReader = new BufferedReader(new FileReader(cityFileName)); // Creating a BufferedReader to read the city file
             BufferedReader connectionTxtReader = new BufferedReader(new FileReader(connectionFileName))) { // Creating a BufferedReader to read the connection file

            String cityTxtLine;
            while ((cityTxtLine = cityTxtReader.readLine()) != null) { // Reading each line in the city file
                String[] cityInfo = cityTxtLine.split(","); // Splitting the line by comma
                if (cityInfo.length == 3) { // Checking if the line is divided into three parts
                    String cityName = cityInfo[0]; // Getting the city name
                    double xCoord = Double.parseDouble(cityInfo[1]); // Getting the X coordinate
                    double yCoord = Double.parseDouble(cityInfo[2]); // Getting the Y coordinate
                    City city = new City(cityName, xCoord, yCoord); // Creating a city object
                    cities.add(city); // Adding the city to the list
                }
            }

            String connectionTxtLine;
            while ((connectionTxtLine = connectionTxtReader.readLine()) != null) { // Reading each line in the connection file
                String[] cityInfo = connectionTxtLine.split(","); // Splitting the line by comma
                if (cityInfo.length == 2) { // Checking if the line is divided into two parts
                    String city1Name = cityInfo[0]; // Getting the name of the first city
                    String city2Name = cityInfo[1]; // Getting the name of the second city
                    addConnection(cities, city1Name, city2Name); // Adding connections
                }
            }
        } catch (IOException | NumberFormatException e) { // In case of file reading or number format error
            e.printStackTrace(); // Printing error tracking information
        }

        return cities; // Returning the list of cities
    }


    //The purpose of this method is to establish a bidirectional connection between two cities.
// It locates the respective city objects based on the given city names and adds each city
// to the connection lists of the other. This creates a two-way connection between the cities.
    public static void addConnection(List<City> cities, String city1Name, String city2Name) {
        City city1 = getCityByName(cities, city1Name); // Get the first city from the list of cities
        City city2 = getCityByName(cities, city2Name); // Get the second city from the list of cities
        if (city1 != null && city2 != null) { // If both cities are found
            city1.getConnections().add(city2Name); // Connect the first city to the second city
            city2.getConnections().add(city1Name); // Connect the second city to the first city (bidirectional connection)
        }
    }


    //This method is used to draw a map of cities and their connections.
// It sets up the canvas and scales, draws the background map image,
// plots each city with its name, and draws lines between connected cities.
// The start and end cities are highlighted in light blue.
    public static void drawMap(List<City> cities, String startCityName, String endCityName) {
        StdDraw.enableDoubleBuffering(); // Enable double buffering
        StdDraw.setCanvasSize(2377 / 2, 1055 / 2); // Set canvas size
        StdDraw.setXscale(0, 2377); // Set x-scale
        StdDraw.setYscale(0, 1055); // Set y-scale
        StdDraw.setPenRadius(0.002); // Set pen radius here

        StdDraw.picture(2377 / 2, 1055 / 2, "map.png", 2377, 1055); // Draw the background map image

        for (City city : cities) { // Loop through each city in the list
            if (city.getCityName().equalsIgnoreCase(startCityName) && city.getCityName().equalsIgnoreCase(endCityName)) {
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE); // Set pen color to light blue for start and end cities
                StdDraw.point(city.getXCoordinate(), city.getYCoordinate()); // Draw a point for the city
                StdDraw.text(city.getXCoordinate(), city.getYCoordinate() + 20, city.getCityName()); // Draw city name
            } else {
                StdDraw.setPenColor(StdDraw.BLACK); // Set pen color to black for other cities
                StdDraw.point(city.getXCoordinate(), city.getYCoordinate()); // Draw a point for the city
                StdDraw.text(city.getXCoordinate(), city.getYCoordinate() + 20, city.getCityName()); // Draw city name
            }
        }

        StdDraw.setPenColor(StdDraw.GRAY); // Set pen color to gray for connections
        StdDraw.setPenRadius(0.002); // Set pen radius for connections
        for (City city : cities) { // Loop through each city in the list
            for (String connectedCityName : city.getConnections()) { // Loop through connections of each city
                City connectedCity = getCityByName(cities, connectedCityName); // Get connected city object
                if (connectedCity != null) { // If connected city is found
                    StdDraw.line(city.getXCoordinate(), city.getYCoordinate(), // Draw a line between the cities
                            connectedCity.getXCoordinate(), connectedCity.getYCoordinate());
                }
            }
        }

        StdDraw.show(); // Show the drawing
    }

    //This method finds the shortest path between two cities in a graph
// representation of cities and their connections using Dijkstra's algorithm.
    public static List<String> findShortestPath(List<City> cities, String startCityName, String endCityName) {
        // Initialize lists to store shortest path, visited cities, distances, and previous cities
        List<String> shortestPath = new ArrayList<>();
        List<String> visitedCities = new ArrayList<>();
        List<Double> distances = new ArrayList<>(Collections.nCopies(cities.size(), Double.POSITIVE_INFINITY));
        List<String> previousCities = new ArrayList<>(Collections.nCopies(cities.size(), null));

        // Get the start and end cities
        City startCity = getCityByName(cities, startCityName);
        City endCity = getCityByName(cities, endCityName);

        // If start or end city is not found, return null
        if (startCity == null || endCity == null) {
            return null;
        }

        // Initialize the distance from start city to 0
        distances.set(cities.indexOf(startCity), 0.0);

        // Loop until the end city is visited
        while (!visitedCities.contains(endCityName)) {
            // Find the index of the city with minimum distance
            int minIndex = findMinDistanceIndex(distances, visitedCities, cities);
            int currentIndex = findMinDistanceIndex(distances, visitedCities, cities);
            if (currentIndex == -1) {
                break;
            }

            // Mark the current city as visited
            visitedCities.add(cities.get(currentIndex).getCityName());

            // Get the current city
            City currentCity = cities.get(currentIndex);

            // Update distances to neighboring cities
            for (String neighborName : currentCity.getConnections()) {
                if (!visitedCities.contains(neighborName)) {
                    double distanceToNeighbor = distanceBetween(currentCity, getCityByName(cities, neighborName));
                    int neighborIndex = cities.indexOf(getCityByName(cities, neighborName));
                    double totalDistance = distances.get(currentIndex) + distanceToNeighbor;
                    if (totalDistance < distances.get(neighborIndex)) {
                        distances.set(neighborIndex, totalDistance);
                        previousCities.set(neighborIndex, currentCity.getCityName());
                    }
                }
            }
        }

        // Reconstruct the shortest path
        String currentCityName = endCityName;
        while (currentCityName != null) {
            shortestPath.add(0, currentCityName);
            int currentIndex = cities.indexOf(getCityByName(cities, currentCityName));
            currentCityName = previousCities.get(currentIndex);
        }

        // Return the shortest path
        return shortestPath;
    }

    //This method calculates the Euclidean distance between
// two cities represented by their coordinates on a plane.
    public static double distanceBetween(City city1, City city2) {
        // Get the coordinates of the two cities
        double x1 = city1.getXCoordinate();
        double y1 = city1.getYCoordinate();
        double x2 = city2.getXCoordinate();
        double y2 = city2.getYCoordinate();

        // Calculate the Euclidean distance between the two cities
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    //This method searches for a city in a list of cities based on its name.
// If the city is found, it returns the city object; otherwise,
// it returns null. The comparison is case-insensitive.
    public static City getCityByName(List<City> cities, String cityName) {
        // Iterate through each city in the list
        for (City city : cities) {
            // Check if the city name matches the provided cityName (ignoring case sensitivity)
            if (city.getCityName().equalsIgnoreCase(cityName)) {
                // If a match is found, return the city object
                return city;
            }
        }
        // If no match is found, return null
        return null;
    }

    //This method finds the index of the city with the minimum distance in a list of distances,
// considering only the cities that have not been visited yet.
    public static int findMinDistanceIndex(List<Double> distances, List<String> visitedCities, List<City> cities) {
        // Initialize the minimum distance and index
        double minDistance = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        // Iterate through the distances list
        for (int i = 0; i < distances.size(); i++) {
            // Check if the city corresponding to this distance has not been visited yet
            if (!visitedCities.contains(cities.get(i).getCityName())) {
                // Get the distance value at index i
                double distance = distances.get(i);
                // Update the minimum distance and index if the current distance is smaller
                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }
        }

        // Return the index of the city with the minimum distance
        return minIndex;
    }
//This method draws the shortest path on the map.
// It takes a list of cities and the shortest path
// as input and uses StdDraw library to visualize the path on the map.
public static void drawShortestPath(List<City> cities, List<String> shortestPath) {
    StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
    StdDraw.setPenRadius(0.004);

    for (int i = 0; i < shortestPath.size() - 1; i++) {
        City city1 = getCityByName(cities, shortestPath.get(i));
        City city2 = getCityByName(cities, shortestPath.get(i + 1));

        if (city1 != null && city2 != null) {
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE); // Set pen color to light blue for city names
            StdDraw.text(city1.getXCoordinate(), city1.getYCoordinate() + 20, city1.getCityName()); // Draw city name
            StdDraw.text(city2.getXCoordinate(), city2.getYCoordinate() + 20, city2.getCityName()); // Draw city name

            StdDraw.line(city1.getXCoordinate(), city1.getYCoordinate(),
                    city2.getXCoordinate(), city2.getYCoordinate());
        }
    }

    StdDraw.show();
}


//This main method is the entry point of the program.
// It reads city information from files,
// prompts the user to input starting and ending cities,
// finds the shortest path between them, and then
// displays the map with the shortest path highlighted.

    public static void main(String[] args) {
        // Read city information from files
        List<City> cities = readCitiesInfoFromFile("city_coordinates.txt", "city_connections.txt");

        Scanner scanner = new Scanner(System.in);
        String startCityName, endCityName;

        // Prompt the user to input the starting city
        while (true) {
            System.out.print("Enter starting city: ");
            startCityName = scanner.nextLine().toLowerCase();
            // Check if the starting city exists in the city list
            if (getCityByName(cities, startCityName) != null) {
                break;
            } else {
                // Print an error message if the starting city is not found
                System.out.println("City named '" + startCityName + "' not found. Please enter a valid city name.");
            }
        }

        // Prompt the user to input the destination city
        while (true) {
            System.out.print("Enter destination city: ");
            endCityName = scanner.nextLine().toLowerCase();
            // Check if the destination city exists in the city list
            if (getCityByName(cities, endCityName) != null) {
                // If the starting and ending cities are the same
                if (startCityName.equals(endCityName)) {
                    // Draw the map with the starting city highlighted in blue
                    drawMap(cities, startCityName, endCityName);
                    System.out.println("Total Distance: 0.00. Path:" + endCityName);
                    return;
                }
                break;
            } else {
                // Print an error message if the destination city is not found
                System.out.println("City named '" + endCityName + "' not found. Please enter a valid city name.");
            }
        }

        // Find the shortest path between the starting and ending cities
        List<String> shortestPath = findShortestPath(cities, startCityName, endCityName);

        if (shortestPath != null) {
            // If a valid shortest path is found
            if (shortestPath.size() == 1) {
                // If there is no path between the cities
                System.out.println("No path could be found.");
                System.exit(0);
            }
            // Draw the map with the shortest path highlighted
            drawMap(cities, startCityName, endCityName);
            drawShortestPath(cities, shortestPath);
            System.out.printf("Total Distance: %.2f\n", calculateTotalDistance(cities, shortestPath));
            System.out.println("Path: " + String.join(" -> ", shortestPath));
        } else {
            // If no valid path is found
            System.out.println("No path could be found.");
        }

        // Schedule a task to terminate the program after 9 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.exit(0);
            }
        }, 9000);
    }


//This method calculates the total distance of the shortest path between cities.
    public static double calculateTotalDistance(List<City> cities, List<String> shortestPath) {
        double totalDistance = 0.0;

        // Iterate over the shortest path
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            City city1 = getCityByName(cities, shortestPath.get(i));
            City city2 = getCityByName(cities, shortestPath.get(i + 1));

            // If both cities are found
            if (city1 != null && city2 != null) {
                // Add the distance between them to the total distance
                totalDistance += distanceBetween(city1, city2);
            }
        }
        return totalDistance;
    }
}
