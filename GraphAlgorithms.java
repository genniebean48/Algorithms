package graph;
import graph.Graph;
import util.*;
import data.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class that implements Floyd-Warshall and Dijkstras Algorithms. 
 * @author Gennie Cheatham and Sarah McClain
 * @version December 11, 2019
 *
 */
public class GraphAlgorithms {
	/**
	 * Algorithm for Floyd-Warshall. Computes the shortest path between all pairs of 
	 * nodes in the graph.
	 * @param g graph of movieID's
	 * @return 2D array that holds the shortest path
	 */
	public static int[][] floydWarshall(Graph<Integer> g) {
		int n = g.numVertices();
		int results[][] = new int[n+1][n+1];  
        
        for (int i = 1; i<=n; i++) 
            for (int j = 1; j<=n; j++) 
            	if(g.edgeExists(i, j)) {
					results[i][j] = 1;
				}else if(i==j) {
					results[i][j] = 0;
				}
				else {
					results[i][j] = Integer.MAX_VALUE;
				}
        
		//finish filling out the array 
		for(int k=1; k<=n; k++) {
			for(int i=1; i<=n; i++) {
				for(int j=1; j<=n; j++) {
					if(results[i][k] + results[k][j] < results[i][j] & results[i][k] + results[k][j]>0) {
							results[i][j] = results[i][k] + results[k][j];
					}
				}
			}
		}
		return results;//the 2D Array that holds the shortest path;	
	}
	
	/**
	 * Algorithm for Dijkstras. Finds the shortest path between the two nodes
	 * entered by the user.
	 * @param g graph
	 * @param source node to start the algorithm within graph
	 * @return map that reconstructs the shortest path between nodes
	 */
	public static Map<Integer, Integer> dijkstrasAlgorithm(Graph<Integer> g, int source) {
		PriorityQueue Q = new PriorityQueue();//priority queue that holds the movies
		Map<Integer, Integer> prev = new HashMap<Integer, Integer>();//where the shortest path is stored
		
		int[] dist = new int[g.numVertices()+1];//arrayList that overwrites itself for shortest distance
		for(int i =1; i< g.numVertices()+1; i++) {
			if(i == source) {
				dist[source] = 0;
			}else {
				dist[i] = Integer.MAX_VALUE;
			}
		}

		for(Integer vertex : g.getVertices()) {
			Q.push(dist[vertex], vertex);
		}
	
		while(!Q.isEmpty()) {
			int u = Q.topElement();
			Q.pop();
			int alt = 0;
			for(int v : g.getNeighbors(u)) {
					if(dist[u]== Integer.MAX_VALUE) {
						alt = Integer.MAX_VALUE;
					}else {
						alt = dist[u] + 1;
					}
					if(alt <= dist[v]) {
						dist[v] = alt;
						prev.put(v, u);
						Q.changePriority(alt, v);
				}
			}
		}
		return prev;
	}
}
