package graphs;
import java.util.*;
import java.util.stream.Stream;

/**
 * Graph Design: A way for you to develop a graph on your terms to satisfy certain functionality.
 */
public class Graph {

    /**
     * Feel free to develop this class however you like, with either an adjacency list or using nodes. For reference, the staff solution
     * was developed using a combination of an adjacency list and the edge class to incorporate route names. The preferred solution
     * (and potentially the most intuitive) is using the adjacency list HashMap to store the vertices/stops as values and a list of
     * that vertex's edges (i.e. the 'A-C' & 'A-B' edges for vertex 'A') as the value. The vertex can be stored as a Node or just a String!
     *
     */

    /**
     * a map to represent an adjacency list for our graph.
     */
    private Map<String, ArrayList> adjacencyList;

    /**
     * a Node class example you might like to use.
     */
    private class Node {
        String name;
        private HashMap<Node, Integer> neighbors;

        public Node(String name){
            this.name = name;
        }

        public Node(String name, HashMap<Node, Integer> neighbors) {
            this.name = name;
            this.neighbors = neighbors;
        }
    }

    /**
     * an Edge class example you might like to use. Hint: this was particularly helpful in the staff solution.
     */
    private class Edge {
        private String from;
        private String to;
        private String routeName;
        private double weight;

        Edge(String from, Double weight){
            this.from = from;
            this.weight = weight;
        }

        Edge(String from, String to, Double weight, String routeName) {
            this.from = from;
            this.to = to;
            this.weight = weight;
            this.routeName = routeName;
        }
    }

    /**
     * Getting this show on the road! 
     */
    public Graph(){
        adjacencyList = new HashMap<>();
    }

    /** Task 1: Add Transit Paths - add Transit Paths to the graph design you have developed.
     *
     * @param stops
     * @param routeName
     * @param travelTimes
     */
    public void addTransitRoute(List<String> stops, String routeName, List<Double> travelTimes) {
        //Initialize the graph by adding a key for each stop. Check if it has already been added or not.
        for (String stop: stops){
            if (!adjacencyList.containsKey(stop)){
                adjacencyList.put(stop, new ArrayList<Edge>());
            }
        }
        /*making an edge with the .from, .to, and the edge weight being the specified time in travelTimes.
        also adding an identifier for the routeName. */
        for (int i = 0; i < travelTimes.size(); i++){
            //add twice for undirected graph since edges must go both ways
            adjacencyList.get(stops.get(i)).add(new Edge(stops.get(i), stops.get(i + 1), travelTimes.get(i), routeName));
            adjacencyList.get(stops.get(i + 1)).add(new Edge(stops.get(i + 1), stops.get(i), travelTimes.get(i), routeName));
        }
    }

    /** Task 2: Get Transit Paths - get the three transit paths from a start to destination that use the least amount of transfers.
     *          Break ties using the shorter path!
     *
     * @param start
     * @param destination
     * @return a List<List<String>> of vertices and routes for the three minimized transfer paths [[A, C, D, E, F, G, 372, 556], ...].
     * The inner list should be formatted where you add Strings in the sequential order "A" then "C" and all vertices, then "32" and all bus routes etc.
     * i.e. We want an inner list of [A, B, G, 32, 1Line] since the route from A -> B is on route 32 and from B -> G is on the 1Line.
     * Ties are broken using the shorter path!
     * Note: Do not add the same route multiple times for a path! I.e. Only add route "32" once per path.
     */
    public List<List<String>> getTransitPaths(String start, String destination) {
        List<List<String>> curatedPaths;
        //helper method for BFS implementation which returns all paths from a start node to an end node.
        curatedPaths = allPaths(start, destination);
        /*adding the route number of the bus/train of every edge to each path we found in BFS. We will also keep track of the initial
        size of each path and the amount of transfers of each path. We then check for any changes in the route (i.e. if we are going
        from 372 -> 556 we detect the route number is not the same and add +1 to the amount of transfers). */
        HashMap<List<String>, Integer> pathToSize = new HashMap<>();
        HashMap<List<String>, Integer> pathTransfers = new HashMap<>();
        for (List<String> paths : curatedPaths){
            int x = paths.size() - 1;
            paths = addingTransitRoutetoPath(paths, x);
            pathTransfers.put(paths, 0);
            int i = x + 1;
            pathToSize.put(paths, x + 1);
            while (i < paths.size()){
                String route = paths.get(i);
                if (!route.equals(paths.get(i - 1))){
                    pathTransfers.put(paths, pathTransfers.get(paths) + 1);
                }
                i++;
            }
        }
        /*locating the 3 minimum amount of transfer paths using pathTransfers, saving them, and then returning them.
         We also have to address potential duplicate paths by checking if the path to be added already exists in res.*/
        List<List<String>> res = new ArrayList<>();
        while (res.size() != 3) {
            List<String> minPath = minimumTransfer(pathTransfers, pathToSize);
            boolean duplicate = true;
            for (int y = 0; y < res.size(); y++){
                if (res.get(y).equals(minPath)){
                    duplicate = false;
                }
            }
            if (duplicate){
                res.add(minPath);
            } else if (!duplicate) {
                continue;
            }
        }
        return res;
    }

    /**
     * You can use this as a helper to return all paths from a start vertex to an end vertex.
     * Call this in getTransitPaths!
     * This method is designed to help give partial credit in the event that you are unable to finish getTransitPaths!
     *
     * @param start
     * @param destination
     * @return a List<List<String>> of containing all vertices among all paths from start to dest [[A, C, D, E, F, G], ...].
     * Do not add transit routes to this method! You should take care of that in getTransitPaths!
     */
    public List<List<String>> allPaths(String start, String destination){
        List<List<String>> curatedPaths = new ArrayList<>();
        Queue<List<String>> queue = new LinkedList<>();
        List<String> path = new ArrayList<>();
        path.add(start);
        queue.offer(path);
        //BFS to find all paths from start to end vertex
        while (!queue.isEmpty()){
            path = queue.poll();
            String last = path.get(path.size() - 1);
            //only adding to our list of paths if we have reached the destination
            if (last == destination){
                curatedPaths.add(path);
            }
            //using the adjacency list to access all edges for any vertex/stop in the graph.
            List<Edge> lastNode = adjacencyList.get(last);
            for (int i = 0; i < lastNode.size(); i++){
                if (!path.contains(lastNode.get(i).to)){
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(lastNode.get(i).to);
                    queue.offer(newPath);
                }
            }
        }
        return curatedPaths;
    }

    /**
     * Private method that finds the current path with minimum Transfers out of a List of paths.
     * If there are two paths with the same amount of transfers, we tie-break by which path has shorter distance
     *
     * @param pathTransfers - a HashMap mapping each path to its amount of transfers
     * @param pathToSize - a HashMap mapping each path to the amount of stops it has
     * @return the path that has the minimum amount of Transfers
     */
    private List<String> minimumTransfer(HashMap<List<String>, Integer> pathTransfers, HashMap<List<String>, Integer> pathToSize){
        List<List<String>> tiePaths = new ArrayList<>();
        List<String> minPath = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        for (List<String> paths : pathTransfers.keySet()) {
            if (pathTransfers.get(paths) < min) {
                minPath = paths;
                min = pathTransfers.get(paths);
            }
        }
        tiePaths.add(minPath);
        for (List<String> paths : pathTransfers.keySet()){
            if (pathTransfers.get(paths) == min && !tiePaths.contains(paths)){
                tiePaths.add(paths);
            }
        }
        if (tiePaths.size() > 1){
            Double minWeight = Double.MAX_VALUE;
            List<String> pathToReturn = new ArrayList<>();
            for (List<String> path : tiePaths){
                Double currWeight = 0.0;
                for (int i = 0; i < pathToSize.get(path) - 1; i++){
                    List<Edge> mapping = adjacencyList.get(path.get(i));
                    double dupPath = Double.MAX_VALUE;
                    for (Edge edge : mapping){
                        if (edge.to == path.get(i + 1)){
                            if (edge.weight < dupPath){
                                dupPath = edge.weight;
                            }
                        }
                    }
                    currWeight += dupPath;
                }
                if (currWeight < minWeight){
                    minWeight = currWeight;
                    pathToReturn = path;
                }
            }
            pathTransfers.remove(pathToReturn);
            return pathToReturn;
        }
        pathTransfers.remove(minPath);
        return minPath;
    }

    /**
     * Private method that adds all the transit routes to a List of strings that already has the stops of a path.
     * I.e. making the list [A, B, G] become [A, B, G, 32, 1Line]
     *
     * @return a List<String> representing the shortest path from a start to a destination with a coffee shop along the way
     * return in the form of a List where you add Strings in the sequential order "A" then "B" and all vertices, then "32" and all bus routes etc.
     * i.e. We want to return [A, B, G, 32, 1Line] since the route from A -> B is on route 32 and the route from B -> G is on the 1Line.
     */
    private List<String> addingTransitRoutetoPath(List<String> paths, Integer size){
        //finds all routes and adds them to the end of the List<String> paths which already contains all the stops.
        for (int i = 0; i < size; i++){
            ArrayList<Edge> routing =  adjacencyList.get(paths.get(i));
            ArrayList<String> pathsToAdd = new ArrayList<>();
            for (Edge edge : routing){
                if (edge.to == paths.get(i + 1)){
                    pathsToAdd.add(edge.routeName);
                }
            }
            if (pathsToAdd.contains(paths.get(paths.size() - 1))){
                continue;
            } else {
                paths.add(pathsToAdd.get(0));
            }
        }
        return paths;
    }

    /**
     * Task 3: Get Shortest Coffee Path - get the shortest path from start to destination with a coffee shop on the route.
     *
     * @param start
     * @param destination
     * @param coffeeStops
     * @return a List<String> representing the shortest path from a start to a destination with a coffee shop along the way
     * return in the form of a List where you add Strings in the sequential order "A" then "B" and all vertices, then "32" and all bus routes etc.
     * i.e. We want to return [A, B, G, 32, 1Line] since the route from A -> B is on route 32 and from B -> G is on the 1Line.
     * Note: Do not add the same route multiple times for a path! I.e. Only add route "32" once per path.
      */
    public List<String> getShortestCoffeePath(String start, String destination, Set<String> coffeeStops) {
        List<List<String>> allPaths = new ArrayList<>();
        //using a private shortestPath method to get the two segments (start to coffee and coffee to destination) and combining them
        for (String coffeeStop : coffeeStops){
            List<String> firstPath = shortestPath(start, coffeeStop);

            List<String> secondPath = shortestPath(coffeeStop, destination);
            //We need to do this to ensure to don't have a duplicate vertex, since the coffeeStop vertex is in both the first and second List.
            secondPath.remove(0);
            List<String> currPath = Stream.concat(firstPath.stream(), secondPath.stream()).toList();
            allPaths.add(currPath);
        }
        /*We need to traverse all the paths with a coffee shop along the way and test the edge weights to see which is the shortest. We keep track
        of the minimum total distance along each of the paths and compare the total distance along the current path, only updating the minPath if
        we see the total distance to be less than the current saved minimum distance. */
        Double minWeight = Double.MAX_VALUE;
        List<String> shortestCoffeePath = new ArrayList<>();
        for (List<String> path : allPaths){
            Double currWeight = 0.0;
            for (int i = 0; i < path.size() - 1; i++){
                List<Edge> mapping = adjacencyList.get(path.get(i));
                double dupPath = Double.MAX_VALUE;
                for (Edge edge : mapping){
                    if (edge.to == path.get(i + 1)){
                        if (edge.weight < dupPath){
                            dupPath = edge.weight;
                        }
                    }
                }
                currWeight += dupPath;
            }
            if (currWeight < minWeight){
                minWeight = currWeight;
                shortestCoffeePath = path;
            }
        }
        //Using Stream to combine the lists above make them immutable so we copy it over in order to have a mutable list.
        List<String> temp = new ArrayList<>(shortestCoffeePath);
        return addingTransitRoutetoPath(temp, temp.size() - 1);
    }

    /**
     * A helper method used to actually find the shortest path between any start node and destination node.
     * Call this in getShortestCoffeePaths!
     * This method is designed to help give partial credit in the event that you are unable to finish getShortestCoffeePath!
     *
     * @param start
     * @param destination
     * @return a List<String> containing all vertices along the shortest path in the form [A, C, D, E, F, G].
     * Do not add transit routes to this method! You should take care of that in getShortestCoffeePaths!
     */
    public List<String> shortestPath(String start, String destination){
        //Dijkstra's algorithm to find the shortest path from any start to any destination
        List<String> currPath = new ArrayList<>();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(edge -> edge.weight));
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        for (String stop : adjacencyList.keySet()){
            dist.put(stop, Double.MAX_VALUE);
        }
        dist.put(start, 0.0);
        queue.add(new Edge(start, 0.0));
        while (!queue.isEmpty()){
            Edge val = queue.poll();
            if (!visited.add(val.from)){
                continue;
            }
            //Ensuring we terminate once we reach our destination vertex and are not creating a whole Shortest Paths Tree.
            if (val.from.equals(destination)){
                break;
            }
            ArrayList<Edge> perimeter = adjacencyList.get(val.from);
            for (Edge edge : perimeter){
                if (!visited.contains(edge.to)){
                    double updateDist = dist.get(val.from) + edge.weight;
                    if (updateDist < dist.get(edge.to)){
                        dist.put(edge.to, updateDist);
                        prev.put(edge.to, val.from);
                        queue.add(new Edge(edge.to, updateDist));
                    }
                }
            }
        }
        String temp = destination;
        //Ensuring we have no redundancies in our list and also ensuring our list faces the correct direction in that case. 
        if (prev.containsKey(temp) || temp.equals(start)){
            while (temp != null){
                currPath.add(temp);
                temp = prev.get(temp);
            }
            Collections.reverse(currPath);
        }
        return currPath;
    }
}