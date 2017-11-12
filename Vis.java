/** Vis.java  A class responsible for dispatching images to different kinds of vertices
  * Zhuofan Zhang
  * 12/08/2015
  * Project9 CS231
  * Image credit to http://lechuck80.deviantart.com/art/Whatsapp-Emoji-Collection-357910493*/

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

class Vis {
	
	// a hashmap that stores pictures corresponding to strings
	private HashMap<String, BufferedImage> map;
	
	// a list of pictures that indicates directions when the hunter is armed
	private BufferedImage[] directions;
	
	// a dummy pictures that prevents exceptions
	private BufferedImage bugFoo;
	
	// constructor
	public Vis() {
		this.directions = new BufferedImage[4];
		this.map = new HashMap<String, BufferedImage>();
		
		// bug sign
		try {
			this.bugFoo = ImageIO.read(new File("Emoji Symbols-88.png"));
		} catch (IOException e) { this.bugFoo = null; }
		
		// mouse / bat
		try {
			this.map.put("Bat", ImageIO.read(new File("Emoji Natur-04.png")));
		} catch (IOException e) { 
			this.map.put("Bat", this.bugFoo); }
		
		// octopus / wumpus (neither won nor lost)
		try {
			this.map.put("Wumpus", ImageIO.read(new File("Emoji Natur-34.png")));
		} catch (IOException e) { 
			this.map.put("Wumpus", this.bugFoo); }
		
		// arrow / hunter
		try {
			this.map.put("Hunter", ImageIO.read(new File("Emoji Objects-114.png")));
		} catch (IOException e) { 
			this.map.put("Hunter", this.bugFoo); }
		
		// arrow shot in center (not used)
		try {
			this.map.put("Hit", ImageIO.read(new File("Emoji Objects-152.png")));
		} catch (IOException e) { 
			this.map.put("Hit", this.bugFoo); }
		
		// mad face (not used)
		try {
			this.map.put("Fall", ImageIO.read(new File("Emoji Natur-87.png")));
		} catch (IOException e) { 
			this.map.put("Fall", this.bugFoo); }
		
		// delicious / won wumpus
		try {
			this.map.put("WonWumpus", ImageIO.read(new File("Emoji Smiley-39.png")));
		} catch (IOException e) { 
			this.map.put("WonWumpus", this.bugFoo); }
		
		// dead / dead wumpus
		try {
			this.map.put("DeadWumpus", ImageIO.read(new File("Emoji Smiley-44.png")));
		} catch (IOException e) { 
			this.map.put("DeadWumpus", this.bugFoo); }
		
		// skeleton / smells wumpus
		try {
			this.map.put("Skeleton", ImageIO.read(new File("Emoji Smiley-88.png")));
		} catch (IOException e) { 
			this.map.put("Skeleton", this.bugFoo); }
		
		// poop / misleading sign of bats
		try {
			this.map.put("Poop", ImageIO.read(new File("Emoji Smiley-90.png")));
		} catch (IOException e) { 
			this.map.put("Poop", this.bugFoo); }
		
		// water / pit nearby
		try {
			this.map.put("Water", ImageIO.read(new File("Emoji Smiley-97.png")));
		} catch (IOException e) { 
			this.map.put("Water", this.bugFoo); }
		
		// heart with arrow / extra arrow
		try {
			this.map.put("ExtraShot", ImageIO.read(new File("Emoji Smiley-180.png")));
		} catch (IOException e) { 
			this.map.put("ExtraShot", this.bugFoo); }
		
		// the set of directions
		// up
		try {
			this.directions[0] = ImageIO.read(new File("Emoji Smiley-115.png"));
		} catch (IOException e) { 
			this.directions[0] = this.bugFoo; }
		// right
		try {
			this.directions[1] = ImageIO.read(new File("Emoji Smiley-117.png"));
		} catch (IOException e) { 
			this.directions[1] = this.bugFoo; }
		// down
		try {
			this.directions[2] = ImageIO.read(new File("Emoji Smiley-116.png"));
		} catch (IOException e) { 
			this.directions[2] = this.bugFoo; }
		// left
		try {
			this.directions[3] = ImageIO.read(new File("Emoji Smiley-118.png"));
		} catch (IOException e) { 
			this.directions[3] = this.bugFoo; }
	}
	
	// gets corresponding image by the given string
	public BufferedImage getImage(String str) {
		return this.map.get(str);
	}
	
	// gets the set of directions
	public BufferedImage[] getImageSet() {
		return this.directions;
	}
	
	// unit test
	public static void main(String[] args) {
		Vis vis = new Vis();
	}
}