import java.util.*;

public class City {
    private String cityName; // Property to store the name of the city
    private double xCoordinate; // Property to store the X coordinate of the city
    private double yCoordinate; // Property to store the Y coordinate of the city
    private Set<String> connections; // Set to store connections of the city to other cities

    // Constructor for the City class
    public City(String cityName, double xCoordinate, double yCoordinate) {
        this.cityName = cityName;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.connections = new HashSet<>(); // Create a new set to store connections
    }

    // Getter and setter methods

    // Getter method for cityName property
    public String getCityName() {
        return cityName;
    }

    // Setter method for cityName property
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // Getter method for xCoordinate property
    public double getXCoordinate() {
        return xCoordinate;
    }

    // Setter method for xCoordinate property
    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    // Getter method for yCoordinate property
    public double getYCoordinate() {
        return yCoordinate;
    }

    // Setter method for yCoordinate property
    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    // Getter method for connections property
    public Set<String> getConnections() {
        return connections;
    }

    // Setter method for connections property
    public void setConnections(Set<String> connections) {
        this.connections = connections;
    }
}
