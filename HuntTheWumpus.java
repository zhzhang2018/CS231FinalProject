/** HuntTheWumpus.java		the game
  * Zhuofan Zhang
  * 12/08/2015
  * CS231 Lab9		*/
import javax.imageio.ImageIO;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

import java.util.*;
public class HuntTheWumpus {
	// necessary elements
	private Landscape scape;
	private LandscapeDisplay display;
	private Hunter hunter;
	private Wumpus wumpus;
	private Graph graph;
	private Arrow arrow;
	private Pit[] pits;
	private Bat[] bats;
	private Vis vis;
	
	// properties of the game itself
	private int Row;
	private int Col;
	private int scale;
	private Random rr = new Random();
	// win = 1 > 0 if user wins; = -1 < 0 if user loses; = 0 if not yet finished
	private int win;
	
	public enum GameState {START, PLAY, END, QUIT}
	public GameState state;
	
	private JLabel textMessage;
	
	// constructor
	public HuntTheWumpus(int row, int col, int scale) {
		this.Row = row;
		this.Col = col;
		this.scale = scale;
		
		this.vis = new Vis();
		this.graph = new Graph();
		this.scape = new Landscape(col, row);
		this.display = new LandscapeDisplay(scape, this.scale);
		
		this.start();
		this.state = GameState.PLAY;
		this.setupUI();
	}
	
	/* Sets up the UI controls for the elevator simulation */
	private void setupUI() {
		// add elements to the UI
		this.textMessage = new JLabel("Preparing the game...");
		JButton restart = new JButton("Start");
		JButton quit = new JButton("Quit");
		JButton showMap = new JButton("Map");
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(this.textMessage);
		panel.add(restart);
		panel.add(quit);
		panel.add(showMap);
		
		this.display.add(panel, BorderLayout.SOUTH);
		this.display.pack();
		
		// listen for keystrokes
		Control control = new Control();
		restart.addActionListener(control);
		quit.addActionListener(control);
		showMap.addActionListener(control);
		this.display.addKeyListener(control);
		this.display.setFocusable(true);
		this.display.requestFocus();
	}
	
	// builds the game
	public void start() {
		this.win = 0;
		
		// creates agents. Bats and pits are stored in arrays to make looping convenient
		this.hunter = new Hunter(rr.nextInt(this.Col), rr.nextInt(this.Row), this.vis);
		this.wumpus = new Wumpus(rr.nextInt(this.Col), rr.nextInt(this.Row), this.vis);
		this.pits = new Pit[(this.Col+this.Row)/3];
		for (int i=0; i<this.pits.length; i++) {
			pits[i] = new Pit(rr.nextInt(this.Col), rr.nextInt(this.Row), this.vis);
		}
		this.bats = new Bat[(this.Col*this.Row)/25];
		for (int i=0; i<this.bats.length; i++) {
			bats[i] = new Bat(rr.nextInt(this.Col), rr.nextInt(this.Row), 
								this.vis, rr.nextInt(45)+5);
		}
		
		
		// builds the graph
		Vertex v0 = new Vertex(0, 0, this.vis);
		this.scape.addAgent(v0);
		Vertex v1 = v0;
		Vertex v2;
		
		// first builds the first column and make it able to loop through itself
		for (int i=1; i<this.Row; i++) {
			v2 = new Vertex(0, i, this.vis);
			this.scape.addAgent(v2);
			this.graph.addEdge(v1, Vertex.Direction.SOUTH, v2);
			v1 = v1.getNeighbor(Vertex.Direction.SOUTH);
		}
		this.graph.addEdge(v1, Vertex.Direction.SOUTH, v0);
		v1 = v0;
		
		// then add the rest, row by row
		for (int i=0; i<this.Row; i++) {
			// adds the row. After this it would look like:
			// 0--1--2--3--4; the 0 here is v0
			// |
			// 0--1--2--3--4; the 4 here is v1
			for (int j=1; j<this.Col; j++) {
				v2 = new Vertex(j, i, this.vis);
				this.scape.addAgent(v2);
				this.graph.addEdge(v1, Vertex.Direction.EAST, v2);
				v1 = v1.getNeighbor(Vertex.Direction.EAST);
			}
			if (i == 0) {
				// 0--1--2--3--4--0
				this.graph.addEdge(v1, Vertex.Direction.EAST, v0);
				v1 = v0.getNeighbor(Vertex.Direction.SOUTH);
			} else {
				// |  |  |  |  |  
				// 0--1--2--3--4--0
				// |
				// 0--1--2--3--4--0  This step creates the last bond
				v0 = v0.getNeighbor(Vertex.Direction.SOUTH);
				this.graph.addEdge(v1, Vertex.Direction.EAST, v0);
				v0 = v0.getNeighbor(Vertex.Direction.NORTH);
				
				// |  |  |  |  |
				// 0--1--2--3--4--0
				// |  |  |  |  |  
				// 0--1--2--3--4--0
				v0 = v0.getNeighbor(Vertex.Direction.WEST);
				for (int k=1; k<this.Col; k++) {
					this.graph.addEdge(v1, Vertex.Direction.NORTH, v0);
					v1 = v1.getNeighbor(Vertex.Direction.WEST);
					v0 = v0.getNeighbor(Vertex.Direction.WEST);
				}
				v0 = v0.getNeighbor(Vertex.Direction.SOUTH);
				v1 = v1.getNeighbor(Vertex.Direction.SOUTH);
				
				// if reaches the bottom, then connect this last row with the first row
				if (i == this.Row-1) {
					for (int k=1; k<this.Col; k++) {
						v1 = v1.getNeighbor(Vertex.Direction.EAST);
						v0 = v0.getNeighbor(Vertex.Direction.EAST);
						this.graph.addEdge(v0, Vertex.Direction.SOUTH, v1);
					}
				}
			}
		}
		v0 = v0.getNeighbor(Vertex.Direction.EAST);
		v0 = v0.getNeighbor(Vertex.Direction.SOUTH);
		v1 = v0;
		this.graph.number(v0);
		
		
		// sets the wumpus into the corresponding vertex
		this.wumpus.setLocation(this.graph.getVertex(this.wumpus.getX(), this.wumpus.getY()));
		
		// deliberately removes some vertices so that the map can become more complicated
		int tempX;
		int tempY;
		for (int i=0; i<rr.nextInt(this.Col*this.Row/4)+this.Col*this.Row/3; i++) {
			// chooses the coordinate of the vertex to be removed
			tempX = rr.nextInt(this.Col);
			tempY = rr.nextInt(this.Row);
			
			// if the hunter / wumpus is at the chosen spot, then give up this round
			if (tempX != this.hunter.getX() && tempY != this.hunter.getY() &&
				tempX != this.wumpus.getX() && tempY != this.wumpus.getY()) {				
				
				v1 = this.graph.getVertex(tempX, tempY);
				// by 25% chance, let the vertex be blank and connectable
				if (rr.nextInt(2) == 0) {
					try {
						this.graph.addEdge(v1.getNeighbor(Vertex.Direction.SOUTH), 
							Vertex.Direction.NORTH, v1.getNeighbor(Vertex.Direction.NORTH));
						this.graph.addEdge(v1.getNeighbor(Vertex.Direction.WEST), 
							Vertex.Direction.EAST, v1.getNeighbor(Vertex.Direction.EAST));
						this.graph.deleteEdge(v1, Vertex.Direction.SOUTH); 
						this.graph.deleteEdge(v1, Vertex.Direction.NORTH);
						this.graph.deleteEdge(v1, Vertex.Direction.WEST); 
						this.graph.deleteEdge(v1, Vertex.Direction.EAST);
					} catch (NullPointerException e) {}
				} 
				// by 25% * 33.3% chance, let the vertex block vertical pathway
				else if (rr.nextInt(3) == 0) {
					this.graph.deleteEdge(v1, Vertex.Direction.SOUTH); 
					this.graph.deleteEdge(v1, Vertex.Direction.NORTH);
				} 
				// by 25% * 33.3% * 33.3% chance, let the vertex block horizontal pathway
				else if (rr.nextInt(3) == 0) {
					this.graph.deleteEdge(v1, Vertex.Direction.WEST); 
					this.graph.deleteEdge(v1, Vertex.Direction.EAST);
				} 
				// for the rest, let the vertex be completely a non-shall-pass hole
				else {
					this.graph.deleteEdge(v1, Vertex.Direction.SOUTH); 
					this.graph.deleteEdge(v1, Vertex.Direction.NORTH);
					this.graph.deleteEdge(v1, Vertex.Direction.WEST); 
					this.graph.deleteEdge(v1, Vertex.Direction.EAST);
				}
			}
		}
		
		// counts steps from the Wumpus
		this.graph.shortestPath(this.wumpus.getLocation());
		
		// sets up pits and gives signs (water) for them
		for (int i=0; i<this.pits.length; i++) {
			this.pits[i].setLocation(this.graph.getVertex(this.pits[i].getX(), this.pits[i].getY()));
			for (Vertex w : this.pits[i].getLocation().getNeighbors()) {
				if (w != null) {
					w.setPit();
				}
			}
		}
		
		// sets up bats and poop signs (somewhat misleading signs) for them
		// bats leave poop next to its vertex by chance
		// poop stays even when bats move
		for (int i=0; i<this.bats.length; i++) {
			this.bats[i].setLocation(this.graph.getVertex(this.bats[i].getX(), this.bats[i].getY()));
			for (Vertex w : this.bats[i].getLocation().getNeighbors()) {
				if (w != null && rr.nextInt(3) == 0) {
					w.setBat();
				}
			}
		}
		
		// finds the right location for the hunter
		v1 = v0;
		System.out.println(this.hunter.getX()+" hunter "+this.hunter.getY());
		this.hunter.setLocation(this.graph.getVertex(this.hunter.getX(), this.hunter.getY()));
		
		// if close to the wumpus / a pit / stuck in a block, sends the hunter to 
		// another place to start
		while (this.hunter.getLocation().getCost() <= 2 || 
				this.hunter.getLocation().getCost() >= this.Row+this.Col || 
				this.hunter.getLocation().getPit()) {
			this.hunter.setPosition(rr.nextInt(this.Col), rr.nextInt(this.Row));
			this.hunter.setLocation(this.graph.getVertex(this.hunter.getX(), this.hunter.getY()));
		}
		this.hunter.getLocation().setVisible();
		
		// adds all to landscape
		this.scape.addAgent(this.hunter);
		this.scape.addAgent(this.wumpus);
		for (int i=0; i<this.pits.length; i++) {
			this.scape.addAgent(this.pits[i]);
		}
		for (int i=0; i<this.bats.length; i++) {
			this.scape.addAgent(this.bats[i]);
		}
	}
	
	// restarts the game
	public void restart() {
		this.display.dispose();
		this.graph.clear();
		this.scape = new Landscape(this.Col, this.Row);
		this.display = new LandscapeDisplay(this.scape, this.scale);
		this.setupUI();
		this.start();
		this.state = GameState.PLAY;
	}
	
	// shows the whole map
	public void showMap() {
		// lets the wumpus be visible
		this.wumpus.setReplay();
		// lets all pits be visible
		for (int i=0; i<this.pits.length; i++) {
			this.pits[i].setReplay();
		}
		// lets all vertices be visible (and thus all bats)
		for (int i=0; i<this.Row; i++) {
			for (int j=0; j<this.Col; j++) {
				try {
					this.graph.getVertex(j, i).setVisible();
				} catch (NullPointerException e) {}
			}
		}
		this.update();
	}
	
	// updates the game
	public void update() {
		// if we're starting, then restart the game
		if (this.state == GameState.START) {
			this.restart();
		}
		
		// if the game's proceeding, then add some interesting things 
		else if (this.state == GameState.PLAY) {
			this.textMessage.setText("Use wasd to move; press space to hold the arrow");
			// arrow:
			// if we have an arrow in landscape, 
			// 1) draw it if it's in a visible vertex
			// 2) allow the hunter to pick it up, and then delete it
			if (this.arrow != null) {
				if (this.arrow.getPicked()) {
					this.scape.removeAgent(this.arrow);
					this.arrow = null;
				} else if (this.hunter.getX() == this.arrow.getX() && 
							this.hunter.getY() == this.arrow.getY()) {
					this.hunter.pick();
					this.arrow.pick();
					System.out.println("You have picked up an extra arrow.");
					System.out.println("You must use your arrows at the same time, so that you might hunt the wumpus before it gets up and eats you.");
				}
			}
			this.display.update();
			// add an arrow to the landscape by chance.
			// the hunter can shoot multiple times altogether when having more arrows
			if (rr.nextInt(200) < 3) {
				this.arrow = new Arrow(rr.nextInt(this.Col), rr.nextInt(this.Row), this.vis);
				this.arrow.setLocation(this.graph.getVertex(this.arrow.getX(), this.arrow.getY()));
				this.scape.addAgent(this.arrow);
				System.out.println(this.arrow.getX()+" arrow "+this.arrow.getY());
			}
			
			// bats:
			// 1) a bat moves automatically or moves when captures a hunter
			// 2) a bat rests for 5 rounds (5*450ms) before moving for the next time.
			//	  during that time, the hunter can escape from its paws.
			for (int i=0; i<this.bats.length; i++) {
				// if captures a hunter and finished resting, then take the hunter
				// to a place where it won't be stuck
				if (this.bats[i].getLocation() == this.hunter.getLocation() 
					&& this.bats[i].getCount() >= 5) {
					this.bats[i].move(this.graph.getVertex(rr.nextInt(Col), rr.nextInt(Row)));
					while (this.bats[i].getLocation().getNeighbors().isEmpty()) {
						this.bats[i].move(this.graph.getVertex(rr.nextInt(Col), rr.nextInt(Row)));
					}
					this.hunter.setLocation(this.bats[i].getLocation());
					this.hunter.getLocation().setVisible();
					
					// after a bat-aided operation, determines if the game should be ended
					// first see if the hunter has been killed by the wumpus
					if (this.win > 0) {
						this.wumpus.setWin(-1);
						this.state = GameState.END;
					} else if (this.win < 0) {
						this.wumpus.setWin(1);
						this.state = GameState.END;
					}
					// then see if the hunter has fallen into a pit
					for (int k=0; k<this.pits.length; k++) {
						if (this.hunter.getLocation() == this.pits[k].getLocation()) {
							this.win = -1;
							this.pits[k].setWin(1);
							this.state = GameState.END;
						}
					}
				} 
				// if flies automatically
				else {
					if (this.bats[i].canMove()) {
						this.bats[i].move(this.graph.getVertex(rr.nextInt(Col), rr.nextInt(Row)));
					}
				}
			}
		} 
		
		// if the game has ended, give the results.
		else if (this.state == GameState.END) {
			this.display.update();
			if (this.win > 0) {
				this.textMessage.setText("You won. Press Start to play again.");
			} else if (this.win < 0) {
				if (this.wumpus.getWin() > 0) {
					this.textMessage.setText("You are delicious. Press Start to feed the Wumpus again.");
				} else {
					this.textMessage.setText("A pit loved you so much that it flew you to the moon.");
				}
			}
		}
	}
	
	// Provides simple keyboard control to the game by implementing the KeyListener interface
	private class Control extends KeyAdapter implements ActionListener {
		/* Controls the simulation in response to key presses */
		public void keyTyped(KeyEvent e) {
			if (state == GameState.PLAY) {
				if (("" + e.getKeyChar()).equalsIgnoreCase("a")) {
					win = hunter.move(Vertex.Direction.WEST);
					System.out.println("left"); }
				else if (("" + e.getKeyChar()).equalsIgnoreCase("s")) {
					win = hunter.move(Vertex.Direction.SOUTH);
					System.out.println("down"); }
				else if (("" + e.getKeyChar()).equalsIgnoreCase("d")) {
					win = hunter.move(Vertex.Direction.EAST);
					System.out.println("right"); }
				else if (("" + e.getKeyChar()).equalsIgnoreCase("w")) {
					win = hunter.move(Vertex.Direction.NORTH);
					System.out.println("up"); }
				else if (("" + e.getKeyChar()).equalsIgnoreCase(" ")) {
					hunter.arm();
					win = hunter.testShot();
					System.out.println("space"); }
				if (hunter.getLocation() == wumpus.getLocation()) {
					win = -1;
				}
			}
			// after an key operation, determines if the game should be ended
			// first see if the hunter has been killed by the wumpus
			if (win > 0) {
				wumpus.setWin(-1);
				state = GameState.END;
			} else if (win < 0) {
				wumpus.setWin(1);
				state = GameState.END;
			}
			// then see if the hunter has fallen into a pit
			for (int i=0; i<pits.length; i++) {
				if (hunter.getLocation() == pits[i].getLocation()) {
					win = -1;
					pits[i].setWin(1);
					state = GameState.END;
				}
			}
		}

		public void actionPerformed(ActionEvent event) {
			System.out.println(event.getActionCommand());
			if (event.getActionCommand().equalsIgnoreCase("Start")) {
				state = GameState.START; }
			else if (event.getActionCommand().equalsIgnoreCase("Quit")) {
				state = GameState.QUIT; }
			else if (event.getActionCommand().equalsIgnoreCase("Map")) {
				if (state == GameState.END) {
					showMap(); }
				else { System.out.println("The map is preferred to be seen after the game is finished.");
						System.out.println("We can't hear your keystrokes now. Please start again and control your hand.");
						state = GameState.END;
						showMap(); } }
		}
	}
	
	// runs the game
	public static void main(String[] args) throws InterruptedException {
		// initialize the simulation
		HuntTheWumpus game = new HuntTheWumpus(15, 15, 50);
		// run simulation until terminated
		while (game.state != GameState.QUIT) {
			Thread.sleep(450);
			game.update();
		}
		
		// clean up and close the application
		game.display.dispose();
	}
}