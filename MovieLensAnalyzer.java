package analyzer;
import java.util.Scanner;
import java.util.Map;
import data.*;
import graph.*;
import util.*;
import java.util.InputMismatchException;

/**
 ** No extra credit implemented but thank you for a great semester! Sorry we bugged you during the Holiday party!!
 * @author Gennie Cheatham and Sarah McClain
 * @version December 11, 2019
 */
public class MovieLensAnalyzer{
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres		
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}		
		System.out.println("========================= Welcome to MoveLens Analyzer ==================");
		System.out.println("The files being analyzed are: ");
		System.out.println(args[0]);
		System.out.println(args[1]);
		DataLoader loader = new DataLoader();
		loadingData(args[0], args[1], loader);
		
		//shows the user options for establishing adjacency
		System.out.println("There are 2 choices for defining adjacency:");
		System.out.println("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies.");
		System.out.println("[Option 2] u and v are adjacent if the same 12 users watch both movies (regardless of rating)");
		System.out.println("Choose an option to build the graph. (1-2)");
		
		//takes in the option for establishing adjacency
		Scanner scan = new Scanner(System.in);
		Graph<Integer> g = new Graph<Integer>();
		try {
			String option = scan.nextLine();
			if(option.equals("1")) {
				System.out.println("Building graph...");
				g = buildGraph1(loader);
			}
			else if(option.equals("2")) {
				System.out.println("Building graph...");
				g = buildGraph2(loader);
			}else {
				System.out.println("Please enter a number 1-2.");
				scan.close();
			}
		}catch(InputMismatchException e) {
			throw new InputMismatchException();
		}

		String option2;
		Scanner scan2 = new Scanner(System.in);
		try {
			do {
				//prints out the analyzing options for the user
				System.out.println();
				System.out.println("[Option 1] Print out statistics about the graph.");
				System.out.println("[Option 2] Print node information.");
				System.out.println("[Option 3] Display shortest path between two nodes.");
				System.out.println("[Option 4] Quit.");

				//prompts the user to choose an option when analyzing the graph
				System.out.println("Choose an option. (1-4)");
				option2 = scan.nextLine();
				if(option2.equals("1")) { //printing out option 1 information
					graphStats(g);
				}else if(option2.equals("2")) { //printing out option 2 information
					System.out.println("Enter movie id (1-1000): ");
					int movieID = scan2.nextInt();
					nodeStats(loader, g, movieID);
				}else if(option2.equals("3")) { //printing out option 3 information	
					findShortestPath(loader, g);
				}else if(!option2.equals("4")) {
					System.out.println("Please enter a number 1-4.");
				}
			}while(!option2.equals("4"));
			System.out.println("Exiting... bye.");
			System.exit(0);
		}catch(InputMismatchException e) {
			throw new InputMismatchException();
		}
		
		
		


	}
	
/**
 * Static method to build the graph such that edges are only made when two movies
 * have 12 of the same viewers who gave the same rating
 * @param loader contains hashmaps of the movie data
 */
public static Graph<Integer> buildGraph1(DataLoader loader){
	int sharedUsers = 0;
	Graph<Integer> graph = new Graph<Integer>();
	Map<Integer, Movie> movies = loader.getMovies();
	for(Movie m : movies.values()) {
		graph.addVertex(m.getMovieId());
		for(Movie k : movies.values()) {
			graph.addVertex(k.getMovieId());
			sharedUsers = 0;
			if(m.equals(k)==false) {
				for(Integer u : m.getRatings().keySet()) {
					if(k.getRatings().containsKey(u)) {
						if(m.getRatings().get(u).equals(k.getRatings().get(u))) {		
							sharedUsers++;
						}
					}
					if(sharedUsers>=12) {
						graph.addEdge(m.getMovieId(), k.getMovieId());
						graph.addEdge(k.getMovieId(), m.getMovieId());
						break;
					}
				}
			}
		}
		
	}
	return graph;
}

/**
 * Static method to build the graph such that edges are only made when two movies
 * have 12 of the same viewers
 * @param loader contains hashmaps of movie data 
 */
public static Graph<Integer> buildGraph2(DataLoader loader) {
	int sharedUsers = 0;
	Graph<Integer> graph = new Graph<Integer>();
	Map<Integer, Movie> movies = loader.getMovies();
	for(Movie m : movies.values()) {
		graph.addVertex(m.getMovieId());
		for(Movie k : movies.values()) {
			graph.addVertex(k.getMovieId());
			sharedUsers = 0;
			if(m.equals(k)==false) {
				for(Integer u : m.getRatings().keySet()) {
					if(k.getRatings().containsKey(u) == true) {
						sharedUsers++;
					}
					if(sharedUsers>=12) {
						graph.addEdge(m.getMovieId(), k.getMovieId());
						graph.addEdge(k.getMovieId(), m.getMovieId());
						break;
					}
				}
			}
		}
		
	}
	return graph;
}

/**
 * Static method that uses Data Loader to read in the data.
 * @param movieFile file holding the movie data
 * @param reviewFile file holding the review data
 * @param loader loads in the files 
 */
public static void loadingData(String movieFile, String reviewFile, DataLoader loader) {
	loader.loadData(movieFile, reviewFile);
}

/**
 * Static method that print out Option 1 statistics about the graph
 * @param Graph g, graph to be analyzed
 */
public static void graphStats(Graph<Integer> g) {
	int vertices = g.numVertices();
	int edges = g.numEdges();
	System.out.println("Number of nodes: " + vertices);
	System.out.println("Number of edges: " + edges);
	double density = (edges / (double) (vertices*(vertices-1)));
	System.out.println("Density of the graph: " + density);
	int maxVertex = g.maxVertex();
	int degreeValue = g.degree(maxVertex);
	System.out.println("Maximum degree: " + degreeValue + "(node " + maxVertex + ")");
	int[][] shortestPath = g.floydWarshall(g);
	longPath(shortestPath);
	double avg = avgShortestPath(shortestPath);
	System.out.println("Average length of the shortest paths: " + avg );
}

/**
 * Static method that print out Option 2 statistics about the node
 * 
 * @param loader data from csv files
 * @param Graph g, graph to be analyzed
 * @param movieID, the movie the stats are about
 */
public static void nodeStats(DataLoader loader, Graph<Integer> g, Integer movieID) {
	Map<Integer, Movie> movies = loader.getMovies();
	Movie movie = movies.get(movieID);
	System.out.println(movie.toString());
	System.out.println("Neighbors: ");
	for (Integer n : g.getNeighbors(movieID)) {
		System.out.println("\t" + movies.get(n).getTitle());
	}
}




/**
 * Finds the average shortest path in the graph. 
 * @param shortestPath 2D array holding the shortest path to each node
 * @return average path length
 */
public static double avgShortestPath(int[][] shortestPath) {
	double totalLength = 0.0;
	int paths = 0;
	for(int i=1; i<shortestPath.length; i++) {
		for(int j=1; j<shortestPath.length; j++) {
			if(shortestPath[i][j]!= Integer.MAX_VALUE) {
				totalLength += shortestPath[i][j];
				paths++;
			}
		}
	}
	double avgPath = (double)(totalLength)/(paths);
	return avgPath;
}


/**
 * Finding the Longest Shortest Path. 
 * @param shortestPath holds the shortest path from each node in the graph 
 * @return the longest path in the graph 
 */
public static int longPath(int[][] shortestPath) {
	int longPath = 0;
	int start = 0;
	int end = 0;
	for(int i=1; i<shortestPath.length; i++) {
		for(int j=1; j<shortestPath.length; j++) {
			if(shortestPath[i][j]>longPath && shortestPath[i][j] != Integer.MAX_VALUE) {
				longPath = shortestPath[i][j];
				start = i;
				end = j;
			}
		}
	}
	System.out.println("Diameter: " + longPath + " (from " + start + " to " + end + ")");
	return longPath;
	
}

/**
 * Finds the shortest path between the start and end node entered in by
 * the user. Prints that path
 * @param loader data from csv files
 * @param g graph that holds movieIDs
 */
public static void findShortestPath(DataLoader loader, Graph<Integer> g ) {
	Scanner scan = new Scanner(System.in);
	System.out.println("Enter starting node (1-1000): ");
	int startNode = scan.nextInt();
	System.out.println("Enter ending node: ");
	int endNode = scan.nextInt();
	Map<Integer, Integer> path = g.dijkstrasAlgorithm(g, startNode);
	Map<Integer, Movie> movies = loader.getMovies();
	int currentMovieID = endNode;
	if(path.get(currentMovieID)==null) {
		System.out.println("This node has no neighbors");
		System.exit(-1);
	}
	int parentMovieID = path.get(currentMovieID);
	while(parentMovieID != startNode){
		System.out.println(movies.get(currentMovieID).getTitle() + "==>" 
				+ movies.get(parentMovieID).getTitle());
		currentMovieID = parentMovieID;
		parentMovieID = path.get(parentMovieID);
	}
	System.out.println(movies.get(currentMovieID).getTitle() + "==>" 
			+ movies.get(parentMovieID).getTitle());
}
}

