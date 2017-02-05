package model;

import java.util.ArrayList;

/**
 * WordPlacement.java - An object that represents a word's position, orientation and overlaps on the board.
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
public class WordPlacement {
	private Vector2 startPosition;
	private Orientation orientation;
	private ArrayList<Word> overlaps;

	public WordPlacement( Vector2 startPosition, Orientation orientation, ArrayList<Word> overlaps ) {
		this.startPosition = startPosition;
		this.orientation = orientation;
		this.overlaps = overlaps;
	}

	public WordPlacement( Vector2 startPosition ) {
		this( startPosition, Orientation.UNDEFINED, new ArrayList<Word>(  ) );
	}

	public WordPlacement( Vector2 startPosition, Orientation orientation ) {
		this( startPosition, orientation, new ArrayList<Word>(  ) );
	}

	public WordPlacement( WordPlacement wordPlacement ){
		this( wordPlacement.getStartPosition(), wordPlacement.getOrientation(), wordPlacement.getOverlaps() );
	}

	/**
	 * Get the grid position of the first letter of the word.
	 * @return x,y position of where parent word starts.
	 */
	public Vector2 getStartPosition() {
		return startPosition;
	}

	/**
	 * Set the grid position of the first letter of the word.
	 * @param startPosition x,y position of where parent word should start.
	 */
	public void setStartPosition( Vector2 startPosition ) {
		this.startPosition = startPosition;
	}

	/**
	 * The orientation of the parent word.
	 * @return Horizontal or Vertical
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * Set how the word will be oriented on the board.
	 * @param orientation the Horizontal or Vertical orientation the parent word should have.
	 */
	public void setOrientation( Orientation orientation ) {
		this.orientation = orientation;
	}

	/**
	 * Get a list of all the overlap references this placement has.
	 * @return
	 */
	public ArrayList<Word> getOverlaps() {
		return overlaps;
	}

	/**
	 * Add an overlap reference to this word placement.
	 * @param word
	 */
	public void addOverlap( Word word ){
		overlaps.add( word );
	}

	/**
	 * Remove a overlap reference from this word placement.
	 * @param word
	 */
	public void removeOverlap( Word word ){
		overlaps.remove( word );
	}
}
