/** Hunter.java		the hunter in the game
  * Zhuofan Zhang
  * 12/08/2015
  * CS231 Lab9		*/
import java.awt.Graphics;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Hunter extends Cell {
	
	// how many arrows does the hunter have
	private int shoot;
	
	private Vertex location;
	private Vis vis;
	
	// if the hunter is ready to shoot
	public boolean armed;
	
	// if the hunter has shot an arrow or not
	private boolean shot;
	
	// constructor
	public Hunter(int x0, int y0, Vis vis) {
		super(x0, y0);
		this.shoot = 1;
		this.vis = vis;
		this.armed = false;
		this.shot = false;
	}
	
	// shoots away an arrow
	public void hunt() {
		this.shoot --;
	}
	
	// picks up an arrow
	public void pick() {
		this.shoot ++;
		System.out.println("Picked up an arrow");
	}
	
	// arms the arrow or not
	public void arm() {
		this.armed = !this.armed;
	}
	
	public boolean getShot() {
		return this.shot;
	}
	
	// returns 1 if wins, -1 if loses (e.g. alarms the Wumpus), 0 if nothing happens
	public int move(Vertex.Direction dir) {
		// if the hunter chooses to hit the wall with its head
		if (this.location.getNeighbor(dir) == null) {
			return 0;
		}
		
		// if the hunter is just walking, not wanting to shoot
		if (!this.armed) {
			// goes to the indicated direction
			this.setLocation(this.location.getNeighbor(dir));
			// sets the new vertex to visible
			this.location.setVisible();
			return 0;
		}
		// if the hunter is ready to shoot
		else {
			// show that the hunter has already shot an arrow.
			// this would alarm the wumpus and get the hunter eaten, unless the hunter
			// shoots another arrow that kills the wumpus immediately
			this.shot = true;
			// if the hunter shoots the wumpus
			if (this.location.getNeighbor(dir).wumpus) {
				return 1;
			} 
			// if the hunter didn't hit the wumpus, then the hunter loses an arrow
			else {
				this.hunt();
			}
			// if the hunter runs out of arrows and doesn't kill the wumpus, 
			// then get ready to be eaten
			if (this.shoot <= 0) {
				return -1;
			}
		}
		return 0;
	}
	
	// sees if the hunter has shot any arrow.
	// if so, and now the hunter is not armed, then it means that the wumpus has been
	// alarmed and would eat the hunter soon
	public int testShot() {
		if (this.shot && !this.armed) {
			return -1;
		} else {
			return 0;
		}
	}
	
	// sets the location (Vertex)
	public void setLocation(Vertex v) {
		this.location = v;
		this.x = v.getX();
		this.y = v.getY();
	}
	
	public Vertex getLocation() {
		return this.location;
	}
	
	public void updateState(Landscape scape) {
		
	}
	
	public void draw(Graphics g, int x, int y, int scale) {
		// the hunter is always visible
		BufferedImage img = this.vis.getImage("Hunter");
		for (int i=0; i<this.shoot; i++) {
			g.drawImage(img, x+(this.x*scale+scale/4+2*i), y+(this.y*scale+scale/4+2*i), 
						x+(this.x*scale+3*scale/4+2*i), y+(this.y*scale+3*scale/4+2*i), 
						0, 0, 160, 160, null);
		}
		// if the hunter is ready to shoot, show the signs
		if (this.armed) {
			// up, right, down, left
			BufferedImage[] hands = this.vis.getImageSet();
			g.drawImage(hands[0], x+(this.x*scale+scale/3), y+(this.y*scale), 
				x+(this.x*scale+2*scale/3), y+(this.y*scale+scale/3), 0, 0, 160, 160, null);
			g.drawImage(hands[1], x+(this.x*scale+2*scale/3), y+(this.y*scale+scale/3), 
				x+(this.x*scale+scale), y+(this.y*scale+2*scale/3), 0, 0, 160, 160, null);
			g.drawImage(hands[2], x+(this.x*scale+scale/3), y+(this.y*scale+2*scale/3), 
				x+(this.x*scale+2*scale/3), y+(this.y*scale+scale), 0, 0, 160, 160, null);
			g.drawImage(hands[3], x+(this.x*scale), y+(this.y*scale+scale/3), 
				x+(this.x*scale+scale/3), y+(this.y*scale+2*scale/3), 0, 0, 160, 160, null);
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Hunter hunter = new Hunter(4, 5, new Vis());
		Vertex v1 = new Vertex(5, 5, new Vis());
		hunter.setLocation(v1);
		System.out.println(hunter.getLocation());
	}
}