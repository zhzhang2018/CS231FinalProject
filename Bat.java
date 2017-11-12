/** Bat.java		the bat in the game
  * Zhuofan Zhang
  * 12/11/2015
  * CS231 Lab9		*/
import java.awt.Graphics;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.Random;

public class Bat extends Cell {
	
	private Vertex location;
	private Vis vis;
	
	// round indicates how many rounds should pass when this bat moves automatically
	private int round;
	// count counts the current # of passed rounds
	private int count;
	
	private Random rr = new Random();
	
	// constructor
	public Bat(int x0, int y0, Vis vis, int round) {
		super(x0, y0);
		this.vis = vis;
		this.round = round;
		this.count = 1;
	}
	
	// sees if the bat can move automatically
	public boolean canMove() {
		this.count ++;
		return (this.count >= this.round);
	}
	
	public int getCount() {
		return this.count;
	}
	
	// moves the bat to a specific location & resets the count
	public void move(Vertex v) {
		this.setLocation(v);
		this.count = 0;
	}
	
	// moves the bat to a specific location
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
		// if the bat appears in an explored cave and thus can be seen
		if (this.location.getVisible()) {
			img = this.vis.getImage("Bat");
			g.drawImage(img, x+(this.x*scale+scale/10), y+(this.y*scale+scale/10), 
						x+(this.x*scale+9*scale/10), y+(this.y*scale+9*scale/10), 
						0, 0, 160, 160, null);
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Bat bat = new Bat(5, 5, new Vis(), 5);
		Vertex v1 = new Vertex(4, 5, new Vis());
		for (int i=0; i<15; i++) {
			System.out.println(bat.canMove());
			if (bat.canMove()) {
				bat.move(v1);
			}
		}
		bat.setLocation(v1);
		System.out.println(bat.getLocation());
	}
}