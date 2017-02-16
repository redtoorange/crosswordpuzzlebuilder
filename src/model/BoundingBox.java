package model;

/**
 * BoundingBox.java - A helper model class to represent a rectangle on a {@link Grid}.  It represents the extents of the
 * crossword puzzle on the array of {@link GridCell}s inside of the {@link Grid}.
 *
 * @author Andrew McGuiness
 * @version 15/Feb/2017
 */
public class BoundingBox {
	/** The left-most bound. */
	private int minX = Integer.MAX_VALUE;

	/** The right-most bound. */
	private int maxX = Integer.MIN_VALUE;

	/** The lower-most bound. */
	private int minY = Integer.MAX_VALUE;

	/** The upper-most bound. */
	private int maxY = Integer.MIN_VALUE;


	/**
	 * Accessor for the left-most bound.
	 *
	 * @return The current left-most bound.
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * Setter for the left-most bound.
	 *
	 * @param minX The new left-most bound.
	 */
	public void setMinX( int minX ) {
		this.minX = minX;
	}

	/**
	 * Accessor for the right-most bound.
	 *
	 * @return The current left-most bound.
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Setter for the right-most bound.
	 *
	 * @param maxX The new right-most bound.
	 */
	public void setMaxX( int maxX ) {
		this.maxX = maxX;
	}

	/**
	 * Accessor for the lower-most bound.
	 *
	 * @return The current left-most bound.
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Setter for the lower-most bound.
	 *
	 * @param minY The new lower-most bound.
	 */
	public void setMinY( int minY ) {
		this.minY = minY;
	}

	/**
	 * Accessor for the upper-most bound.
	 *
	 * @return The current left-most bound.
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Setter for the upper-most bound.
	 *
	 * @param maxY The new upper-most bound.
	 */
	public void setMaxY( int maxY ) {
		this.maxY = maxY;
	}
}
