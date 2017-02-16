package model;

/**
 * Vector2.java - A representation of a position on a two dimension {@link Grid}.  Used by {@link WordPlacement} to
 * maintain the starting {@link GridCell} of a {@link Word} on the {@link Grid}
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 * @see WordPlacement
 */
public class Vector2 {
	/** The x position this {@link Vector2} represents. */
	private int x;

	/** The y position this {@link Vector2}  represents. */
	private int y;

	/**
	 * Manually specify the x and y positions for the new {@link Vector2} .
	 *
	 * @param x Int x position.
	 * @param y Int y position.
	 */
	public Vector2( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the x and y of the new {@link Vector2} equal to the passed in {@link Vector2}.
	 *
	 * @param vector2 x and y will be copied from this {@link Vector2} into the new {@link Vector2}.
	 */
	public Vector2( Vector2 vector2 ) {
		this( vector2.x, vector2.y );
	}

	/**
	 * Default constructor will set x and y to 0.
	 */
	public Vector2() {
		this( 0, 0 );
	}

	/**
	 * Compare two {@link Vector2}s for equality based on their x, y positions.
	 *
	 * @param otherVector Other {@link Vector2} to be used for comparison.
	 * @return true - if both vectors have the same x and y position.
	 */
	public boolean equals( Vector2 otherVector ) {
		return x == otherVector.x && y == otherVector.y;
	}

	/**
	 * Getter method for the x position.
	 *
	 * @return the current value of x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter method for the x position.
	 *
	 * @param x what to set x to.
	 */
	public void setX( int x ) {
		this.x = x;
	}

	/**
	 * Getter method for the y position.
	 *
	 * @return the current value of y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter method for the y position.
	 *
	 * @param y what to set y to.
	 */
	public void setY( int y ) {
		this.y = y;
	}
}
