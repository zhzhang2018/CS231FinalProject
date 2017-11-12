/** Arrow.java		an extra arrow
  * Zhuofan Zhang
  * 12/11/2015
  * CS231 Lab9		*/
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Arrow extends Cell {
	
	private Vertex location;
	private Vis vis;
	
	// indicates whether the arrow has been picked up or not
	public boolean picked;
	
	// constructor
	public Arrow(int x0, int y0, Vis vis) {
		super(x0, y0);
		this.vis = vis;
		this.picked = false;
	}
	
	// get picked
	public void pick() {
		this.picked = true;
	}
	
	public boolean getPicked() {
		return this.picked;
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
		// gets the corresponding image
		BufferedImage img = this.vis.getImage("ExtraShot");
		
		// if its location is already visible, draw it
		if (this.location.getVisible()) {
			g.drawImage(img, x+(this.x*scale+scale/4), y+(this.y*scale+scale/4), 
				x+(this.x*scale+3*scale/4), y+(this.y*scale+3*scale/4), 0, 0, 160, 160, null);
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Arrow arrow = new Arrow(5, 5, new Vis());
		System.out.println(arrow.getPicked());
		Vertex v1 = new Vertex(4, 5, new Vis());
		arrow.setLocation(v1);
		System.out.println(arrow.getLocation());
	}
}