package model;

/**
 * Vector2.java - A representation of a 2D positon on a grid.  This is mostly a helper data representation to
 * better organize words on an Abstract grid.  Just an easy way to encapsulate an x, y position.
 *
 * @author - Andrew McGuiness
 * @version - 04/Feb/2017
 */
public class Vector2 {
	private int x;
	private int y;

	/**
	 * Manually specify the starting x and y positions for the new vector.
	 * @param x starting x position.
	 * @param y starting y position.
	 */
	public Vector2( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the x and y of the new vector equal to the passed vector.
	 * @param vector2 x and y will be copied from this vector into the new vector.
	 */
	public Vector2( Vector2 vector2){
		this( vector2.x, vector2.y);
	}

	/**
	 * Default constructor will set x and y to position 0.
	 */
	public Vector2(){
		this(0, 0);
	}

	/**
	 * Compare two vectors for equality based on their positions.
	 * @param otherVector any other Vector2.
	 * @return true - if both vectors have the same x and y position.
	 */
	public boolean equals( Vector2 otherVector ) {
		return x == otherVector.x && y == otherVector.y;
	}

	/**
	 * Getter method for the x position.
	 * @return the current value of x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter method for the x position.
	 * @param x what to set x to.
	 */
	public void setX( int x ) {
		this.x = x;
	}

	/**
	 * Getter method for the y position.
	 * @return the current value of y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter method for the y position.
	 * @param y what to set y to.
	 */
	public void setY( int y ) {
		this.y = y;
	}
}
