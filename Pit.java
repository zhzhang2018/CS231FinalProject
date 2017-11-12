/** Pit.java		the pit in the game
  * Zhuofan Zhang
  * 12/11/2015
  * CS231 Lab9		*/
import java.awt.Graphics;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Pit extends Cell {
	
	private Vertex location;
	private Vis vis;
	
	// when replay is true, it means that the user's viewing the whole map, 
	// and so we can draw this pit
	private boolean replay;
	
	// indicates whether the cause of the user's lost is this pit or not
	private int win;
	
	// constructor
	public Pit(int x0, int y0, Vis vis) {
		super(x0, y0);
		this.vis = vis;
		this.win = 0;
		this.replay = false;
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
	}
	
	public Vertex getLocation() {
		return this.location;
	}
	
	public void updateState(Landscape scape) {
		
	}
	
	public void draw(Graphics g, int x, int y, int scale) {
		BufferedImage img;
		// if wins (player falls)
		if (this.win > 0 || this.replay) {
			img = this.vis.getImage("Fall");
			g.drawImage(img, x+(this.x*scale+scale/10), y+(this.y*scale+scale/10), 
						x+(this.x*scale+9*scale/10), y+(this.y*scale+9*scale/10), 0, 0, 
						160, 160, new Color(0.2f, 0.6f, 0.2f), null);
		}
	}
	
	public static void main(String[] args) {
		Pit pit = new Pit(5, 5, new Vis());
		System.out.println(pit.getWin());
		Vertex v1 = new Vertex(4, 5, new Vis());
		pit.setLocation(v1);
		System.out.println(pit.getLocation());
	}
}