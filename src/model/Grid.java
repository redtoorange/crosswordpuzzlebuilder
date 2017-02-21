package model;

import java.util.ArrayList;

/**
 * Grid.java - Represents a {@link Grid} of {@link GridCell}s, each one containing a letter.  The {@link Grid} maintains
 * an {@link ArrayList} of {@link Word}s that are currently on it.  This is a purely model class, that must be created
 * using a {@link control.GridController}.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 * @see control.GridController
 * @see GridCell
 * @see Word
 */
public class Grid {
	/** The int representing the number of columns in the {@link Grid}. */
	private int width;

	/** The int representing the number of rows in the {@link Grid}. */
	private int height;

	/** The two dimensional array of {@link GridCell}s. */
	private GridCell[][] letterGrid;

	/**
	 * All of the {@link Word}s that are on this {@link Grid}.  These are maintained to prevent GC of the {@link Word}s
	 * if the {@link DictionaryFile} that contains them is ever deleted.
	 */
	private ArrayList< Word > wordsOnGrid;

	/**
	 * Initialize a {@link Grid} on a what the {@link control.GridController} feeds in.
	 *
	 * @param width       The final cell count width of the {@link Grid} after generation by the {@link
	 *                    control.GridController}.
	 * @param height      The final cell count height of the {@link Grid} after generation by the {@link
	 *                    control.GridController}.
	 * @param letterGrid  The 2D array of {@link GridCell}s that this {@link Grid} is based on.
	 * @param wordsOnGrid The {@link ArrayList} of all {@link Word}s on this {@link Grid}.
	 */
	public Grid( int width, int height, GridCell[][] letterGrid, ArrayList< Word > wordsOnGrid ) {
		this.width = width;
		this.height = height;

		this.letterGrid = letterGrid;
		this.wordsOnGrid = wordsOnGrid;
	}

	/**
	 * Get the current {@link Grid} width.
	 *
	 * @return Int {@link Grid} width in columns.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the current {@link Grid} height.
	 *
	 * @return Int {@link Grid} height in rows.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the 2D array of {@link GridCell}s.
	 *
	 * @return 2D array of {@link GridCell} references.
	 */
	public GridCell[][] getLetterGrid() {
		return letterGrid;
	}

	/**
	 * Get the {@link ArrayList} of all the {@link Word}s on this {@link Grid}.
	 *
	 * @return {@link ArrayList} containing all the {@link Word}s on the {@link Grid}.
	 */
	public ArrayList< Word > getWordsOnGrid() {
		return wordsOnGrid;
	}
}