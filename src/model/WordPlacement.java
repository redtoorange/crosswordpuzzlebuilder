package model;

import java.util.ArrayList;

/**
 * WordPlacement.java - An object that represents a {@link Word}'s position, {@link Orientation} and other {@link Word}s
 * it overlaps with on the {@link Grid}.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 * @see Word
 */
public class WordPlacement {
	/** The {@link GridCell} position to place the first letter of the {@link Word}. */
	private Vector2 startPosition;
	
	/** The {@link Orientation} of the {@link Word}. */
	private Orientation orientation;

	/** The {@link ArrayList} of other {@link Word}s that this {@link WordPlacement} overlap with. */
	private ArrayList< Word > overlaps;

	/**
	 * Fully initialize constructor.  Used by the other constructors.
	 *
	 * @param startPosition The {@link GridCell} position to place the first letter of the {@link Word}.
	 * @param orientation   The {@link Orientation} of the {@link Word}.
	 * @param overlaps      {@link ArrayList} of other {@link Word}s that this {@link WordPlacement} should overlap
	 *                      with.
	 */
	public WordPlacement( Vector2 startPosition, Orientation orientation, ArrayList< Word > overlaps ) {
		this.startPosition = startPosition;
		this.orientation = orientation;
		this.overlaps = overlaps;
	}

	/**
	 * Constructor with a {@link Vector2} start position and will set the {@link Orientation} to UNDEFINED.
	 *
	 * @param startPosition The {@link GridCell} position to place the first letter of the {@link Word}.
	 */
	public WordPlacement( Vector2 startPosition ) {
		this( startPosition, Orientation.UNDEFINED, new ArrayList< Word >( ) );
	}

	/**
	 * Constructor with an {@link Orientation} and a {@link Vector2} start position.
	 *
	 * @param startPosition The {@link GridCell} position to place the first letter of the {@link Word}.
	 * @param orientation   The {@link Orientation} of the {@link Word}.
	 */
	public WordPlacement( Vector2 startPosition, Orientation orientation ) {
		this( startPosition, orientation, new ArrayList< Word >( ) );
	}

	/**
	 * Copy constructor for the {@link WordPlacement} class, copies the {@link Vector2} position and {@link Orientation}
	 * into this {@link WordPlacement}.
	 *
	 * @param wordPlacement {@link WordPlacement} to clone into this {@link WordPlacement}.
	 */
	public WordPlacement( WordPlacement wordPlacement ) {
		this( wordPlacement.getStartPosition( ), wordPlacement.getOrientation( ), wordPlacement.getOverlaps( ) );
	}

	/**
	 * Get the {@link Grid} position of the first letter of the {@link Word}.
	 *
	 * @return {@link Vector2} position of where parent {@link Word} starts.
	 */
	public Vector2 getStartPosition() {
		return startPosition;
	}

	/**
	 * Set the {@link GridCell} position on the {@link Grid} of the first letter of the {@link Word}.
	 *
	 * @param startPosition {@link Vector2} position of where parent {@link Word} should start.
	 */
	public void setStartPosition( Vector2 startPosition ) {
		this.startPosition = startPosition;
	}

	/**
	 * Get {@link Orientation} of the parent {@link Word}.
	 *
	 * @return {@link Orientation} of the {@link Word} (Vertical or Horizontal).
	 * @see Orientation
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * Set how the {@link Word} will be {@link Orientation} on the {@link Grid}.
	 *
	 * @param orientation the Horizontal or Vertical {@link Orientation} the parent {@link Word} should have.
	 */
	public void setOrientation( Orientation orientation ) {
		this.orientation = orientation;
	}

	/**
	 * Get a {@link ArrayList} of all the overlapping {@link Word} references this {@link WordPlacement} has.
	 *
	 * @return {@link ArrayList} of {@link Word}s that this {@link WordPlacement} would overlap with.
	 */
	public ArrayList< Word > getOverlaps() {
		return overlaps;
	}

	/**
	 * Add an overlapped {@link Word} reference to this {@link WordPlacement}.
	 *
	 * @param word The {@link Word} to add to the overlaps.
	 */
	public void addOverlap( Word word ) {
		overlaps.add( word );
	}

	/**
	 * Remove a overlapped {@link Word} reference from this {@link WordPlacement}.
	 *
	 * @param word The {@link Word} to remove from the overlaps.
	 */
	public void removeOverlap( Word word ) {
		overlaps.remove( word );
	}
}
