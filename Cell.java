// Bruce Maxwell
// CS 231 Fall 2012
// Project 7
// Modified so that x and y are int values

import java.awt.Graphics;
import java.util.*;

/*
    A parent class for Cell objects

    This class has location functionality

    This is an abstract class, because two methods are abstract.

    updateState( Landscpae scape ): updates the Cell's state

    draw( Graphics g, int x, int y, int scale ): draw the Cell into the window

 */
public abstract class Cell {
	// location is a global property of all Cell subclasses
	int x;
	int y;


	public Cell(int x0, int y0) {
		x = x0;
		y = y0;
		//System.out.println( this.getClass().getName() + " made at " + x + ", " + y );
	}

	public void setPosition(int tx, int ty) {
		x = tx;
		y = ty;
		//System.out.println( this.getClass().getName() + " position set to " + x + ", " + y );
	}

	public int getX() {
		return(x);
	}

	public int getCol() {
		return( (int)(x + 0.5) );
	}

	public int getY() {
		return(y);
	}

	public int getRow() {
		return( (int)(y + 0.5) );
	}

	// default parent function returns a period
	public String toString() {
		return( "." );
	}

	// abstract method
	public abstract void updateState( Landscape scape );

	// abstract method
	public abstract void draw(Graphics g, int x, int y, int scale);

};
