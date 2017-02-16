package model;

/**
 * Orientation.java - The orientation of a word on the board.  A word without an orientation should default to
 * UNDEFINED. Used primaryily by {@link WordPlacement} and the {@link control.GridController} to determine the direction
 * a {@link Word} should be placed when it is printed and when determining overlaps.  The {@link
 * control.PuzzleImageController} uses it to determine the category that the definition falls under (Across vs Down).
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 * @see control.PuzzleImageController
 * @see WordPlacement
 * @see control.GridController
 */
public enum Orientation {
	HORIZONTAL, VERTICAL, UNDEFINED
}
