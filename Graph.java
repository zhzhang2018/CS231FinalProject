/** Graph.java		a Graph class that maintains a list of vertices (Vertex objects)
  * Zhuofan Zhang
  * 12/01/2015
  * CS231 Lab9		*/
import java.util.ArrayList;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.io.*;
public class Graph {
	
	private ArrayList<Vertex> list;
	private Comp comp;
	private HashMap<String, Vertex> map;
	
	public Graph() {
		list = new ArrayList<Vertex>();
		comp = new Comp();
		map = new HashMap<String, Vertex>();
	}
	
	public void clear() {
		this.list.clear();
		this.map.clear();
	}
	
	// counts number of vertices
	public int vertexCount() {
		return this.list.size();
	}
	
	/*  adds v1 and v2 to the graph (if necessary) and adds edges connecting v1 to v2 
		via direction dir and connecting v2 to v1 via the opposite direction		*/
	public void addEdge(Vertex v1, Vertex.Direction dir, Vertex v2) {
		if (!list.contains(v1) && v1 != null) {
			list.add(v1);
		}
		if (!list.contains(v2) && v2 != null) {
			list.add(v2);
		}
		v1.connect(v2, dir);
		v2.connect(v1, v1.opposite(dir));
	}
	
	public void deleteEdge(Vertex v1, Vertex.Direction dir) {
		try {
			v1.getNeighbor(dir).disconnect(v1.opposite(dir));
		} catch (NullPointerException e) {}
			v1.disconnect(dir);
	}
	
	// uses hashmap to record every vertex
	// can only be used when the grid is complete
	// the input v0 should be the Vertex at the upper left corner
	// regards the grid as larger than or equal to 2x2
	public void number(Vertex v0) {
		// finding out the size of the grid
		Vertex v1 = v0.getNeighbor(Vertex.Direction.EAST);
		int col = 1;
		int row = 1;
		while (v1 != v0) {
			col += 1;
			v1 = v1.getNeighbor(Vertex.Direction.EAST);
		}
		v1 = v0.getNeighbor(Vertex.Direction.SOUTH);
		while (v1 != v0) {
			row += 1;
			v1 = v1.getNeighbor(Vertex.Direction.SOUTH);
		}
		// numbering each vertex
		for (int i=0; i<row; i++) {
			for (int j=0; j<col; j++) {
				this.map.put(j+" "+i, v0);
				v0 = v0.getNeighbor(Vertex.Direction.EAST);
			}
			v0 = v0.getNeighbor(Vertex.Direction.SOUTH);
		}
	}
	
	// gets the vertex in the corresponding location by using hashmap
	// accurate when you see the graph as a whole grid without holes
	public Vertex getVertex(int j, int i) {
		return this.map.get(j+" "+i);
	}
	
	/*
	Implements a single-source shortest-path algorithm for the Graph. 
	This method finds the cost of the shortest path between v0 and every other connected 
	vertex in the graph, counting every edge as having unit cost. 
	Dijkstra's algorithm is used. It works only for graphs with non-negative weight edges.
	*/
	public void shortestPath(Vertex v0) {
		// Initialize all vertices to be unmarked and have infinite cost
		for (Vertex i : this.list) {
			i.setMarked(false);
			i.setCost(Integer.MAX_VALUE);
		}
		
		// Create a priority queue, q, that orders vertices by lowest cost
		// Set the cost of v0 to 0 and add it to q
		PriorityQueue<Vertex> q = new PriorityQueue<Vertex>(this.vertexCount(), this.comp);
		Vertex v;
		v0.setCost(0);
		q.add(v0);
		
		// while q is not empty
		while (q.size() != 0) {
			// let v be the vertex in q with lowest cost, remove v from q, and mark v as visited
			v = q.poll();
			v.setMarked(true);
			
			for (Vertex w : v.getNeighbors()) {
				if (w != null) {
					if (!w.getMarked() && v.getCost()+1 < w.getCost()) {
						w.setCost(v.getCost()+1);
					
						/*  It is possible that the vertex w is already in the priority queue. 
							Adding the vertex a second time will lead to duplicate references 
							to w in the queue. So, at the last step, remove w, then add w.	*/
						q.remove(w);
						q.add(w);
					}
				}
			}
		}
	}
	
	public class Comp implements Comparator<Vertex> {
		public int compare(Vertex v1, Vertex v2) {
			return v1.compareTo(v2);
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Vis vis = new Vis();
		Graph graph = new Graph();
		Vertex[] list = new Vertex[10];
		for (int i=0; i<10; i++) {
			list[i] = new Vertex(i, i, vis);
		}
		
		graph.addEdge(list[2], Vertex.Direction.EAST, list[5]);
		graph.addEdge(list[8], Vertex.Direction.WEST, list[5]);
		graph.addEdge(list[1], Vertex.Direction.EAST, list[9]);
		graph.addEdge(list[7], Vertex.Direction.EAST, list[4]);
		graph.addEdge(list[3], Vertex.Direction.WEST, list[0]);
		graph.addEdge(list[2], Vertex.Direction.SOUTH, list[1]);
		graph.addEdge(list[4], Vertex.Direction.SOUTH, list[5]);
		graph.addEdge(list[3], Vertex.Direction.NORTH, list[5]);
		graph.addEdge(list[9], Vertex.Direction.NORTH, list[8]);
		graph.addEdge(list[6], Vertex.Direction.NORTH, list[7]);
		
		System.out.println(graph.vertexCount());
		int count;
		for (int i=0; i<10; i++) {
			System.out.println("Testing vertex #"+i);
			graph.shortestPath(list[i]);
			count = 0;
			for (Vertex w : list) {
				System.out.println("This is vertex #"+count+":\n"+w);
				count ++;
			}
		}
	}
	
}