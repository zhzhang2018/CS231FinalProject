/** Vertex.java		basic unit in Graph class
  * Zhuofan Zhang
  * 12/01/2015
  * CS231 Lab9		*/
import java.util.HashMap;

import java.util.Collection;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Vertex extends Cell implements Comparable<Vertex> {
	
	// the enum class that indicates directions
	public enum Direction { NORTH, EAST, SOUTH, WEST }
	
	// The Vertex class needs to maintain a Map of edges. 
	// Each entry in the edge map has a Direction as key and a Vertex as value.
	// I created my own last project, and I'm using the built-in one just for convenience.
	private HashMap<Direction, Vertex> map;
	
	// These fields will be used during graph traversal.
	private int cost;
	private boolean marked;
	
	// visible or not
	private boolean visible;
	
	// Vis is a file that sends corresponding pictures to the file
	private Vis vis;
	
	// states if this vertex has the following things
	// public boolean hunter;
	public boolean wumpus;
	
	// if there's a pit nearby or not
	private boolean pit;
	
	// if there's a bat nearby or not
	private boolean bat;
	
	// constructor
	public Vertex(int x, int y, Vis vis) {
		super(x, y);
		this.map = new HashMap<Direction, Vertex>();
		this.visible = false;
		this.vis = vis;
	}
	
	// returns the compass opposite of the given direction
	public static Direction opposite(Direction d) {
		if (d == Direction.NORTH) {
			return Direction.SOUTH;
		} else if (d == Direction.SOUTH) {
			return Direction.NORTH;
		} else if (d == Direction.WEST) {
			return Direction.EAST;
		} else {
			return Direction.WEST;
		}
	}
	
	// get & set methods
	public int getCost() {
		return this.cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public boolean getMarked() {
		return this.marked;
	}
	
	public void setMarked(boolean mark) {
		this.marked = mark;
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	public void setVisible() {
		this.visible = true;
	}
	
	public boolean getPit() {
		return this.pit;
	}
	
	public void setPit() {
		this.pit = true;
	}
	
	public boolean getBat() {
		return this.bat;
	}
	
	public void setBat() {
		this.bat = true;
	}
	
	// only one-direction connection
	public void connect(Vertex other, Direction dir) {
		this.map.put(dir, other);
	}
	
	// removes a connection
	public void disconnect(Direction dir) {
		this.map.remove(dir);
	}
	
	// gets a neighbor in the specific direction
	public Vertex getNeighbor(Direction dir) {
		return this.map.get(dir);
	}
	
	// gets all neighbors
	public Collection<Vertex> getNeighbors() {
		return this.map.values();
	}
	
	public int compareTo(Vertex other) {
		if (this.cost < other.getCost()) {
			return -1;
		} else if (this.cost > other.getCost()) {
			return 1;
		} else {
			return 0;
		}
	}
	
	// Print out the number of neighbors, the cost, and the marked flag.
	public String toString() {
		return "# of Neighbors: "+this.map.size()+"\nCost: "+this.cost+"\nMarked: "+this.marked+"\n";
	}
	
	public void updateState(Landscape scape) {
		
	}
	
	public void draw(Graphics g, int x, int y, int scale) {
		// draw only when visible
		if (this.visible) {
			// if located near the wumpus, paint the background red; otherwise, brown
			if (this.cost <= 2) {
				g.setColor(new Color(0.5f, 0.2f, 0.2f));
			} else {
				g.setColor(new Color(0.5f, 0.5f, 0.1f));
			}
			
			// draws a corresponding small rectangle to indicate that there's a connection
			// to another vertex in one direction
			if (this.map.get(Direction.SOUTH) != null) {
				g.fillRect(x+(this.x*scale+scale/4), y+(this.y*scale+scale/2), 
							scale/2, scale/2);
			}
			if (this.map.get(Direction.NORTH) != null) {
				g.fillRect(x+(this.x*scale+scale/4), y+this.y*scale, scale/2, scale/2);
			}
			if (this.map.get(Direction.WEST) != null) {
				g.fillRect(x+this.x*scale, y+(this.y*scale+scale/4), scale/2, scale/2);
			}
			if (this.map.get(Direction.EAST) != null) {
				g.fillRect(x+(this.x*scale+scale/2), y+(this.y*scale+scale/4), 
							scale/2, scale/2);
			} 
			if (!this.getNeighbors().isEmpty()) {
				g.fillRect(x+(this.x*scale+scale/10), y+(this.y*scale+scale/10), 
							4*scale/5, 4*scale/5);
			}
			
			// again, if the Wumpus is near
			if (this.cost <= 2 && !this.getNeighbors().isEmpty()) {
				BufferedImage sign = this.vis.getImage("Skeleton");
				g.drawImage(sign, x+(this.x*scale+scale/4), y+(this.y*scale+scale/4), 
							x+(this.x*scale+3*scale/4), y+(this.y*scale+3*scale/4), 
							0, 0, 160, 160, new Color(0.5f, 0.2f, 0.2f), null);
			}
			
			// if a pit is near
			if (this.pit) {
				BufferedImage sign = this.vis.getImage("Water");
				g.drawImage(sign, x + (this.x*scale+scale/4), y + (this.y*scale+scale/4), 
							x + (this.x*scale+3*scale/4), y + (this.y*scale+3*scale/4), 
							0, 0, 160, 160, new Color(0.2f, 0.6f, 0.2f), null);
			}
			
			// if a bat is near, or used to be near
			if (this.bat) {
				BufferedImage sign = this.vis.getImage("Poop");
				g.drawImage(sign, x + (this.x*scale+scale/5), y + (this.y*scale+scale/5), 
							x + (this.x*scale+3*scale/5), y + (this.y*scale+3*scale/5), 
							0, 0, 160, 160, null);
			}
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Vertex v1 = new Vertex(1, 2, new Vis());
		Direction[] dir = new Direction[] {Direction.SOUTH, Direction.NORTH, 
											Direction.EAST, Direction.WEST};
		for (int i=0; i<4; i++) {
			System.out.println(dir[i].toString());
			System.out.println(v1.opposite(dir[i]).toString());
		}
		
		Vertex v2 = new Vertex(3, 4, new Vis());
		v1.setCost(12450);
		v2.setCost(23333);
		System.out.println(v1.compareTo(v2));
		System.out.println(v2.compareTo(v1));
		
		v1.connect(v2, Direction.SOUTH);
		System.out.println(v1);
		System.out.println(v2);
	}
}