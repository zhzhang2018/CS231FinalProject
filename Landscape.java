// Bruce Maxwell
// CS 231 Fall 2012
// Project 7
// Modified so that all double values are int now
import java.util.*;

public class Landscape {
	private int width; 
	private int height;
	private LinkedList<Cell> agents;

	public Landscape(int rows, int cols) {
		agents = new LinkedList<Cell>();
		height = rows;
		width = cols;
	}

	public void reset() {
		// get rid of all of the agents
		agents.clear();
	}

	// modify to round
	public int getRows() {
		return (int)(height + 0.5);
	}

	// add method
	public int getHeight() {
		return height;
	}

	// modify to round
	public int getCols() {
		return (int)(width + 0.5);
	}

	// add method
	public int getWidth() {
		return width;
	}

	public void addAgent(Cell a) {
		agents.add(a);
	}

	public void removeAgent( Cell a ) {
		agents.remove(a);
	}

	/*public ArrayList<Cell> getAgents() {
		return agents.toArrayList();
	}*/
	public LinkedList<Cell> getAgents() {
		return agents;
	}

	public String toString() {
		ArrayList<String> s = new ArrayList<String>();

		for(int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				s.add(" ");
			}
			s.add("\n");
		}

		for( Cell item: agents ) {
			int r = item.getRow();
			int c = item.getCol();

			if(r >= 0 && r < height && c >= 0 && c < width ) {
				int index = r * (this.getCols() + 1) + c;
				s.set( index, item.toString() );
			}
		}

		String sout = "";
		for( String a: s ) {
			sout += a;
		}

		return sout;
	}

	/*public void advance() {
		// put the agents in random oder
		ArrayList<Cell> items = agents.toShuffledList();

		// update the state of each agent
		for(Cell item: items ) {
			item.updateState( this );
		}
	}*/
	public void advance() {
		
		// update the state of each agent
		for(Cell item: agents ) {
			item.updateState( this );
		}
	}
	
	public static void main(String argv[]) {
		int rows = 30;
		int cols = 70;
		int N = 300;
		Landscape scape = new Landscape(rows, cols);
		Random gen = new Random();

		
	}

};
