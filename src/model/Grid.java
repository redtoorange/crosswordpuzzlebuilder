package model;

import java.util.ArrayList;

/**
 * Grid.java - This class represents a Grid of letters.  It can take in a DictionaryFile and build the
 * grid.
 *
 * @author - Andrew McGuiness
 * @version - 04/Feb/2017
 */
public class Grid {
	private int width;
	private int height;
	private GridCell[][] letterGrid;
	private ArrayList< Word > wordsOnGrid;

	/**
	 * Initialize a grid based on a what the GridController feeds in.
	 * @param width The final cell count width of the grid after generation
	 * @param height The final cell count height of the grid after generation
	 * @param letterGrid The 2D array of GridCells that this grid is based on.
	 * @param wordsOnGrid The List of all words on this grid.
	 */
	public Grid( int width, int height,  GridCell[][] letterGrid, ArrayList< Word > wordsOnGrid) {
		this.width = width;
		this.height = height;

		this.letterGrid = letterGrid;
		this.wordsOnGrid = wordsOnGrid;
	}

	/**
	 * Get the current grid width.
	 * @return grid width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the current grid height.
	 * @return grid height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the 2D array of GridCells.
	 * @return 2D array of object references.
	 */
	public GridCell[][] getLetterGrid() {
		return letterGrid;
	}

	/**
	 * Get a list of all the words on the Grid.
	 * @return ArrayList containing all the words on the grid.
	 */
	public ArrayList< Word > getWordsOnGrid() {
		return wordsOnGrid;
	}
}