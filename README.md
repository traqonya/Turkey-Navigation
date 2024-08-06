# Turkey-Navigation

Here’s a README description you can use for your GitHub project:

---

# Turkey Navigation

**Turkey Navigation** is a Java application that finds the shortest path between cities based on their geographical coordinates and connections. Utilizing Dijkstra's algorithm, this program efficiently calculates the optimal route, visualizing the results on a map.

## Features

- **City Representation**: Each city is represented with its name and coordinates (X, Y).
- **Bidirectional Connections**: Cities can connect to each other, allowing for bidirectional travel.
- **Shortest Path Calculation**: The program implements Dijkstra's algorithm to determine the shortest route between any two cities.
- **Map Visualization**: A graphical representation of cities and their connections is displayed using the StdDraw library, with the shortest path highlighted.
- **User-Friendly Input**: Users can input the starting and destination cities, with validation to ensure valid entries.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed on your machine.
- StdDraw library for graphical visualization.
- Input files:
  - `city_coordinates.txt`: Contains the names and coordinates of cities.
  - `city_connections.txt`: Contains the connections between cities.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/turkey-navigation.git
   ```
2. Navigate to the project directory:
   ```bash
   cd turkey-navigation
   ```
3. Compile the Java files:
   ```bash
   javac *.java
   ```
4. Run the application:
   ```bash
   java BaranOnder
   ```

### Usage

1. When prompted, enter the starting city name.
2. Next, enter the destination city name.
3. The application will display the map with cities and their connections, highlighting the shortest path and calculating the total distance.
