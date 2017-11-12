/** Wumpus.java		the Wumpus in the game
  * Zhuofan Zhang
  * 12/08/2015
  * CS231 Lab9		*/
import java.awt.Graphics;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Wumpus extends Cell {
	
	private Vertex location;
	private Vis vis;
	// replay = true indicates that the user wants to see the map
	private boolean replay;
	
	// win = true if the wumpus ate the hunter; = false if shot by the hunter
	private int win;
	
	public Wumpus(int x0, int y0, Vis vis) {
		super(x0, y0);
		this.vis = vis;
		this.win = 0;
	}
	
	public void setWin(int win) {
		this.win = win;
	}
	
	public int getWin() {
		return this.win;
	}
	
	public void setReplay() {
		this.replay = true;
	}
	
	public void setLocation(Vertex v) {
		this.location = v;
		this.x = v.getX();
		this.y = v.getY();
		// marks the corresponding vertex as "having the wumpus"
		v.wumpus = true;
	}
	
	public Vertex getLocation() {
		return this.location;
	}
	
	public void updateState(Landscape scape) {
		
	}
	
	public void draw(Graphics g, int x, int y, int scale) {
		BufferedImage img;
		// if dead
		if (this.win < 0) {
			img = this.vis.getImage("DeadWumpus");
			g.drawImage(img, x + (this.x*scale+scale/10), y + (this.y*scale+scale/10), 
							x + (this.x*scale+9*scale/10), y + (this.y*scale+9*scale/10), 
							0, 0, 160, 160, null);
		} 
		// if wins
		else if (this.win > 0) {
			img = this.vis.getImage("WonWumpus");
			g.drawImage(img, x + (this.x*scale+scale/10), y + (this.y*scale+scale/10), 
							x + (this.x*scale+9*scale/10), y + (this.y*scale+9*scale/10), 
							0, 0, 160, 160, null);
		}
		// if showing replay
		if (this.replay) {
			img = this.vis.getImage("Wumpus");
			g.drawImage(img, x + (this.x*scale+scale/10), y + (this.y*scale+scale/10), 
							x + (this.x*scale+9*scale/10), y + (this.y*scale+9*scale/10), 
							0, 0, 160, 160, null);
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Wumpus wumpus = new Wumpus(5, 5, new Vis());
		Vertex v1 = new Vertex(4, 5, new Vis());
		wumpus.setLocation(v1);
		System.out.println(wumpus.getLocation());
	}
}