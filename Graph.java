package graph;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

/**
 * A class that implements a Graph with a Hashmap of integers as the keys, and 
 * array lists of integers as the values. 
 * 
 * @author Gennie Cheatham, Sarah McClain
 *
 */
public class Graph<V> extends GraphAlgorithms implements GraphIfc<V> {
	protected Map<V, List<V>> directedGraph;
	protected int numEdges;
	
	/**
	 * Constructor for directed graph.
	 */
	public Graph(){
		directedGraph = new HashMap<V, List<V>>();
		numEdges = 0;
	}
	
	/**
	 * Returns the number of vertices in the graph
	 * @return The number of vertices in the graph
	 */
	@Override
	public int numVertices() {
		return directedGraph.size();
	}

	/**
	 * Returns the number of edges in the graph
	 * @return The number of edges in the graph
	 */
	@Override
	public int numEdges() {
		return numEdges;
	}
	
	/**
	 * Removes all vertices from the graph
	 */
	@Override
	public void clear() {
		directedGraph.clear();
		numEdges = 0;
		
	}

	/**
	 * Adds a vertex to the graph. This method has no effect if the vertex already exists in the graph. 
	 * @param v The vertex to be added
	 * 
	 */
	@Override
	public void addVertex(V v) {
		if(containsVertex(v) == false ) {
			List<V> list = new ArrayList<V>();
			directedGraph.put(v, list);
		}
		
	}

	/** 
	 * Adds an edge between vertices u and v in the graph. 
	 * @param u A vertex in the graph
	 * @param v A vertex in the graph
	 * @throws IllegalArgumentException if either vertex does not occur in the graph.
	 */
	@Override
	public void addEdge(V u, V v) {
		//throws error if either vertex is not in map
		if(containsVertex(u)==false ||
				containsVertex(v) == false) {
			throw new IllegalArgumentException();
		}
		
		if(edgeExists(u, v) ==false) {
		//adds vertex to list of edges
			List<V> listU = directedGraph.get(u);
			listU.add(v);
			numEdges++;
		}
		
	}

	/**
	 * Returns the set of all vertices in the graph.
	 * @return A set containing all vertices in the graph
	 */
	@Override
	public Set<V> getVertices() {
		return directedGraph.keySet();
	}
	
	/**
	 * Returns the neighbors of v in the graph. A neighbor is a vertex that is connected to
	 * v by an edge. If the graph is directed, this returns the vertices u for which an 
	 * edge (v, u) exists.
	 *  
	 * @param v An existing node in the graph
	 * @return All neighbors of v in the graph.
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	@Override
	public List<V> getNeighbors(V v) {
		if(containsVertex(v) == false){
			throw new IllegalArgumentException();
		}
		List<V> listV = directedGraph.get(v);
		return listV;
	}

	/**
	 * Determines whether the given vertex is already contained in the graph. The comparison
	 * is based on the <code>equals()</code> method in the class V. 
	 * 
	 * @param v The vertex to be tested.
	 * @return True if v exists in the graph, false otherwise.
	 */
	@Override
	public boolean containsVertex(V v) {
		if(directedGraph.containsKey(v)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether an edge exists between two vertices. In a directed graph,
	 * this returns true only if the edge starts at v and ends at u. 
	 * @param v A node in the graph
	 * @param u A node in the graph
	 * @return True if an edge exists between the two vertices
	 * @throws IllegalArgumentException if either vertex does not occur in the graph
	 */
	@Override
	public boolean edgeExists(V v, V u) {
		if(containsVertex(v)==false || containsVertex(u)==false ) {
			throw new IllegalArgumentException();
		}
		List<V> listV = directedGraph.get(v);
		for(int i=0; i<listV.size(); i++) {
			if(listV.get(i).equals(u)) {
				return true; 
			}
		}
		return false;
	}
	
	/**
	 * Returns the degree of the vertex. In a directed graph, this returns the outdegree of the
	 * vertex. 
	 * @param v A vertex in the graph
	 * @return The degree of the vertex
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	@Override
	public int degree(V v) {
		if(containsVertex(v) == true) {
			int listSize = directedGraph.get(v).size();
			return listSize;
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Returns the Maximum number of outgoing edges of any node. 
	 * 
	 * @return int maximum degree
	 */
	public int maxDegree() {
		int degree = 0;
		for(V v: directedGraph.keySet()) {
			int currDegree = degree(v);
			if(currDegree>degree) {
				degree = currDegree;
			}
		}
		return degree;
	}
	
	/**
	 * Return the vertex with the Maximum degrees. 
	 * 
	 * @return Vertex the vertex with the maximum degree
	 */
	public V maxVertex() {
		int degree = 0;
		V maxV = null;
		for(V v: directedGraph.keySet()) {
			int currDegree = degree(v);
			if(currDegree>degree) {
				maxV = v;
				degree = currDegree;
			}
		}
		return maxV;
	}
	
	
	
	/**
	 * Returns a string representation of the graph. The string representation shows all
	 * vertices and edges in the graph. 
	 * @return A string representation of the graph
	 */
	@Override 
	public String toString() {
		String toString = "";
		for(V v : directedGraph.keySet()){
			List<V> list = directedGraph.get(v);
			toString += "\n" + v + ": ";
			for(int i=0; i<degree(v); i++) {
				toString += list.get(i) + ", ";
				
			}
			
		}
		return toString;
	}

}
