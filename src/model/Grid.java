package model;

import control.CrosswordPuzzleImageController;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Grid.java - Mother-of-pearl.  This class represents a Grid of letters.  It can take in a DictionaryFile and build the
 * grid.
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
public class Grid {
	private int width, height;
	private GridCell[][] letterGrid;
	private ArrayList< Word > wordsOnGrid;


	public Grid( int width, int height, DictionaryFile dictionaryFile ) {
		letterGrid = new GridCell[width][height];
		this.wordsOnGrid = new ArrayList< Word >( );

		this.width = width;
		this.height = height;

		initGrid( );
		resetGridCells( );

		buildPuzzle( dictionaryFile );
	}

	/**
	 * Initialize all the GridCell references in the letterGrid array.
	 */
	private void initGrid( ) {
		for ( int x = 0; x < width; x++ )
			for ( int y = 0; y < height; y++ )
				letterGrid[x][y] = new GridCell( );
	}

	/**
	 * Reset each GridCell in the letterGrid array.
	 */
	private void resetGridCells() {
		for ( int x = 0; x < letterGrid.length; x++ )
			for ( int y = 0; y < letterGrid[x].length; y++ )
				letterGrid[x][y].reset( );
	}

	/**
	 * DESTRUCTIVE: Do a full reset of the grid.  This wipes all references, resets all the word and then builds the grid again.
	 * @param width max width of the grid
	 * @param height max height of the grid
	 * @param dictionaryFile the DictionaryFile that will be used for a WordList
	 */
	public void rebuildPuzzle( int width, int height, DictionaryFile dictionaryFile ){
		this.width = width;
		this.height = height;

		letterGrid = new GridCell[width][height];

		initGrid( );
		clearTheGrid( );

		buildPuzzle( dictionaryFile );
	}

	/**
	 * Fit the words onto the grid, then create an image out of it.
	 * @param dictionaryFile the dictionary file to be use for the word list
	 * @return
	 */
	private void buildPuzzle( DictionaryFile dictionaryFile ) {
		//System.out.println( "building puzzle" );

		WordList words = dictionaryFile.getWordList( );

		//Loop until the board is valid
		boolean validBoard = false;
		while ( !validBoard ) {
			validBoard = attemptToPlaceAllWords( words );

			if ( !validBoard ) {
				//System.out.println( "board invalid, resetting" );
				clearTheGrid( );
				dictionaryFile.reset();
			}
		}

		//If the board is valid, resize the array it is in, then create the image
		if ( validBoard ) {
			//System.out.println( "got a valid board" );
			fitGridToCrossWord( );
		}
	}

	/**
	 * A soft-reset of the grid that doesn't manipulate the letterGrid Array.
	 */
	private void clearTheGrid( ) {
		//System.out.println( "grid reset" );
		resetGridCells( );
		wordsOnGrid.clear( );
	}

	/**
	 * Attempt to find a spot for all the words in the WordList
	 * @param words The WordList to be placed.
	 * @return returns true of all the words in the wordList were placed on the Grid.
	 */
	private boolean attemptToPlaceAllWords( WordList words ) {
		Word currentWord = words.getNextUnplaced( );
		int iterations = words.listSize( ) * words.listSize( );
		//System.out.println( "attempting to place all words: " + iterations );

		while ( currentWord != null && iterations > 0 ) {
			attemptToPlaceWord( currentWord );

			if ( wordsOnGrid.size( ) > 1 )
				validateWordsOnBoard( );

			currentWord = words.getNextUnplaced( );
			iterations--;
		}

		return wordsOnGrid.size( ) == words.listSize( );
	}

	/**
	 * Resize the grid to fit snuggly around the Grid.  Basically clears out empty columns and rows.
	 */
	private void fitGridToCrossWord() {
		//System.out.println( "resizing grid" );
		int lowestX = Integer.MAX_VALUE;
		int highestX = Integer.MIN_VALUE;
		int lowestY = Integer.MAX_VALUE;
		int highestY = Integer.MIN_VALUE;

		for ( int x = 0; x < letterGrid.length; x++ ) {
			for ( int y = 0; y < letterGrid[x].length; y++ ) {
				if ( letterGrid[x][y].getReferences( ).size( ) > 0 ) {
					if ( y < lowestY )
						lowestY = y;
					if ( y > highestY )
						highestY = y;
					if ( x < lowestX )
						lowestX = x;
					if ( x > highestX )
						highestX = x;
				}
			}
		}

		for ( Word w : wordsOnGrid ) {
			Vector2 pos = w.getWordPlacement( ).getStartPosition( );
			int x = pos.getX() - lowestX + 1;
			int y = pos.getY() - lowestY + 1;
			pos.setX( x );
			pos.setY( y );

			//w.getWordPlacement( ).setStartPosition( pos );
		}

		width = highestX - lowestX + 3;
		height = highestY - lowestY + 3;

		GridCell[][] newGrid = new GridCell[width][height];

		for ( int x = 1; x < width - 1; x++ )
			for ( int y = 1; y < height - 1; y++ )
				newGrid[x][y] = letterGrid[x + lowestX - 1][y + lowestY - 1];

		letterGrid = newGrid;
	}

	/**
	 * Try to place the given word on the grid.
	 * @param currentWord Which word to try to allocate a position for.
	 * @return True if the word was successfully placed.
	 */
	private boolean attemptToPlaceWord( Word currentWord ) {
		//System.out.println( "attempting to place " + currentWord.getWordString( ) );
		boolean success = false;

		if ( wordsOnGrid.isEmpty( ) ) {
			placeFirstWord( currentWord );
			success = true;
		}
		else {
			String current = currentWord.getWordString( );

			ArrayList< WordPlacement > letterMatches = findLetterMatches( current );
			ArrayList< WordPlacement > placements = generatePlacements( letterMatches, current );

			WordPlacement bestPlacement = findBestPlacement( placements );

			success = bestPlacement != null;
			if ( success ) {
				currentWord.setWordPlacement( bestPlacement );
				currentWord.setWrittenOnGrid( true );

				writeWordToGrid( currentWord );
				wordsOnGrid.add( currentWord );
			}
		}

		return success;
	}


	/**
	 * Find all letters in all words currently on the board that match a letter in the current word.  Each match will be
	 * encapsulated into a WordPlacement Object that represents a potential overlap location.
	 * @param current The string representation of the current word.
	 * @return A list of all the overlaps that are suitable, represented as a WordPlacement
	 */
	private ArrayList< WordPlacement > findLetterMatches( String current ) {
		//System.out.println( "finding letter matches " + current );
		ArrayList< WordPlacement > potentialLocations = new ArrayList< WordPlacement >( );

		for ( char c : current.toCharArray( ) ) {
			for ( Word w : wordsOnGrid ) {
				char[] letters = w.getWordString( ).toCharArray( );
				for ( int i = 1; i < letters.length; i++ ) {
					if ( letters[i] == c ) {
						Vector2 spot = new Vector2( w.getWordPlacement( ).getStartPosition( ) );
						Orientation orientation = w.getWordPlacement( ).getOrientation( );

						if ( orientation == Orientation.HORIZONTAL )
							spot.setX( spot.getX( ) + i );
						else
							spot.setY( spot.getY( ) + i );

						potentialLocations.add( new WordPlacement( spot, orientation ) );
					}
				}
			}
		}

		return potentialLocations;
	}

	/**
	 * Parse the list of valid overlaps to create a list of valid placements cause an overlap.
	 * @param potentialOverlaps A list of all the overlaps detected.
	 * @param current The string representation of the current word.
	 * @return A list of all the valid WordPlacements that have been generated that would cause the overlap.
	 */
	private ArrayList< WordPlacement > generatePlacements( ArrayList< WordPlacement > potentialOverlaps, String current ) {
		//System.out.println( "generating placements " + current );
		ArrayList< WordPlacement > placements = new ArrayList< WordPlacement >( );

		for ( WordPlacement potentialOverlap : potentialOverlaps ) {
			Vector2 focusPoint = new Vector2( potentialOverlap.getStartPosition( ) );
			char matchedLetter = letterGrid[focusPoint.getX( )][focusPoint.getY( )].getCharacter( );
			int index = current.indexOf( matchedLetter );

			while ( index >= 0 ) {
				//Horizontal placements
				int x = focusPoint.getX( ) - index;
				int y = focusPoint.getY( );

				WordPlacement placement = testPlacement( current, Orientation.HORIZONTAL, x, y );
				if ( placement != null )
					placements.add( placement );

				//Vertical placements
				x = focusPoint.getX( );
				y = focusPoint.getY( ) - index;

				placement = testPlacement( current, Orientation.VERTICAL, x, y );
				if ( placement != null )
					placements.add( placement );

				index = current.indexOf( matchedLetter, index + 1 );
			}
		}

		return placements;
	}

	/**
	 * Test a placement for validity.  If valid, it will be returned an added as a candidate position.
	 * @param current The string representation of the current word.
	 * @param orientation The Orientation to test.
	 * @param startX The starting X position to test.
	 * @param startY The starting Y position to test.
	 * @return Null if invalid, otherwise the placement was determined to be valid.
	 */
	private WordPlacement testPlacement( String current, Orientation orientation, final int startX, final int startY ) {
		//System.out.println( "testing placements " + current );

		WordPlacement placement = null;
		boolean withinBounds = isWithinBounds( orientation, startX, startY, current.length() );

		if ( withinBounds ) {
			ArrayList< Word > possibleOverlaps = new ArrayList< Word >( );
			boolean conflicts = testGridCells( current, orientation, startX, startY, /*MODIFIED*/ possibleOverlaps );

			if ( !conflicts ) {
				conflicts = findPositionConflicts( current, orientation, startX, startY );

				if ( !conflicts )
					placement = new WordPlacement( new Vector2( startX, startY ), orientation, possibleOverlaps );
			}
		}

		return placement;
	}

	/**
	 * Check the beginning and ending of a word to make sure there is a gap.
	 * @param current
	 * @param orientation
	 * @param startX
	 * @param startY
	 * @return
	 */
	private boolean findPositionConflicts( String current, Orientation orientation, int startX, int startY ) {
		boolean conflicts = false;

		if ( orientation == Orientation.HORIZONTAL ) {
			if ( startX + current.length( ) < width && letterGrid[startX + current.length( )][startY].getReferences( ).size( ) != 0 )
				conflicts = true;
			if ( startX - 1 > 0 && letterGrid[startX - 1][startY].getReferences( ).size( ) != 0 )
				conflicts = true;
		}
		else if ( orientation == Orientation.VERTICAL ) {
			if ( startY + current.length( ) < height && letterGrid[startX][startY + current.length( )].getReferences( ).size( ) != 0 )
				conflicts = true;
			if ( startY - 1 > 0 && letterGrid[startX][startY - 1].getReferences( ).size( ) != 0 )
				conflicts = true;
		}

		return conflicts;
	}

	/**
	 * Step through each potential cell that the word would occupy.  If the cell is empty, continue, if not, test the
	 * cell's character to see if it matches the index of the currentWord. If it does, it is a valid overlap.  So it
	 * parses the GridCell's word references and adds them to the potential overlap list.
	 * @param current The string representation of the current word.
	 * @param orientation The orientation of the word to test.
	 * @param startX The starting X position to test.
	 * @param startY The starting Y position to test.
	 * @param possibleOverlaps A List that will contain all word references that the tested placement overlaps.
	 * @return False is there were no conflicts.  True if a cell in the path did not match the word.
	 */
	private boolean testGridCells( String current, Orientation orientation, int startX, int startY, ArrayList< Word > possibleOverlaps ) {
		boolean letterConflicts = false;

		int i = 0;
		while ( !letterConflicts && i < current.length( ) ) {
			char c = getChar( orientation, startX, startY, i );
			if ( c != 0 ) {
				if ( c == current.charAt( i ) ) {
					ArrayList< Word > references = getReferences( orientation, startX, startY, i );
					for ( Word w : references )
						possibleOverlaps.add( w );
				}
				else {
					letterConflicts = true;
				}
			}
			i++;
		}
		return letterConflicts;
	}

	/**
	 * Get a list of references from a GridCell at a specific position.
	 * @param orientation The orientation that needs to be validated.
	 * @param x The starting x position to be tested.
	 * @param y The starting y position to be tested.
	 * @param offset How much to offset the position based on the orientation.
	 * @return A list of references to all the words that overlap the given GridCell.
	 */
	private ArrayList< Word > getReferences( Orientation orientation, final int x, final int y, final int offset ) {
		return orientation == Orientation.HORIZONTAL ?
				letterGrid[x + offset][y].getReferences( ) :
				letterGrid[x][y + offset].getReferences( );
	}

	/**
	 * Get a char from a GridCell at a specified position.
	 * @param orientation The orientation that needs to be validated.
	 * @param x The starting x position to be tested.
	 * @param y The starting y position to be tested.
	 * @param offset How much to offset the position based on the orientation.
	 * @return the char that the GridCell contains.
	 */
	private char getChar( Orientation orientation, final int x, final int y, final int offset ) {
		return orientation == Orientation.HORIZONTAL ?
				letterGrid[x + offset][y].getCharacter( ) :
				letterGrid[x][y + offset].getCharacter( );
	}

	/**
	 * Ensure that a placement will be within the bounds of the grid.
	 * @param orientation The orientation that needs to be validated.
	 * @param x The starting x position to be tested.
	 * @param y The starting y position to be tested.
	 * @param wordLength The length of the word that is being tested.
	 * @return True is the dimensions are within the bounds of the grid.
	 */
	private boolean isWithinBounds( Orientation orientation, final int x, final int y, final int wordLength ) {
		//System.out.println( "checking bounds " );
		boolean withinBounds = false;

		if ( orientation == Orientation.HORIZONTAL )
			withinBounds = ( x >= 0 && x + wordLength < width && y >= 0 && y < height );
		else if ( orientation == Orientation.VERTICAL )
			withinBounds = ( x >= 0 && x < width && y >= 0 && y + wordLength < height );

		return withinBounds;
	}

	/**
	 * Parse the list of all valid placements and determine the one with the most overlaps.  This one will be used to
	 * place the word.
	 * @param placements List of valid placements
	 * @return The best placement determined by number of overlaps.
	 */
	private WordPlacement findBestPlacement( ArrayList< WordPlacement > placements ) {
		//System.out.println( "finding best placement " );
		WordPlacement bestPlacement = null;

		if ( placements.size( ) > 0 ) {
			for ( WordPlacement currentPlacement : placements ) {
				if ( bestPlacement == null || bestPlacement.getOverlaps( ).size( ) < currentPlacement.getOverlaps( ).size( ) )
					bestPlacement = currentPlacement;
			}
		}

		return bestPlacement;
	}

	/**
	 * Write the currentWord onto the grid, populating cells with references and a character if they are empty
	 * @param currentWord The word to write to the board.
	 */
	private void writeWordToGrid( Word currentWord ) {
		//System.out.println( "writing to grid " + currentWord.getWordString( ) );
		WordPlacement wordPlacement = new WordPlacement( currentWord.getWordPlacement( ) );
		String string = currentWord.getWordString( );

		int x = wordPlacement.getStartPosition( ).getX( );
		int y = wordPlacement.getStartPosition( ).getY( );

		for ( int i = 0; i < string.length( ); i++ ) {
			if ( letterGrid[x][y].getReferences( ).size( ) < 1 )
				letterGrid[x][y].setCharacter( string.charAt( i ) );

			letterGrid[x][y].addReference( currentWord );

			if ( wordPlacement.getOrientation( ) == Orientation.HORIZONTAL )
				x++;
			else
				y++;
		}

		addOverlapReferences( currentWord );
	}

	/**
	 * Update all the word that the currentWord overlaps to add a reference of the current word.
	 * @param currentWord
	 */
	private void addOverlapReferences( Word currentWord ) {
		//System.out.println( "writing to overlaps " + currentWord.getWordString( ) );

		for ( Word overlappingWord : currentWord.getWordPlacement( ).getOverlaps( ) )
			overlappingWord.getWordPlacement( ).addOverlap( currentWord );
	}

	/**
	 * Remove a word from the board, this effectively clears the cells if they no longer have any references.
	 * @param currentWord The word to deleted from the board.
	 */
	private void deleteWordFromGrid( Word currentWord ) {
		//System.out.println( "deleting from grid " + currentWord.getWordString( ) );
		WordPlacement wordPlacement = currentWord.getWordPlacement( );
		String w = currentWord.getWordString( );

		int x = wordPlacement.getStartPosition( ).getX( );
		int y = wordPlacement.getStartPosition( ).getY( );

		for ( int i = 0; i < w.length( ); i++ ) {
			if ( letterGrid[x][y].getCharacter( ) == w.charAt( i ) )
				letterGrid[x][y].removeReference( currentWord );

			if ( wordPlacement.getOrientation( ) == Orientation.HORIZONTAL )
				x++;
			else
				y++;
		}
		removeOverlapReferences( currentWord );
	}

	/**
	 * When a word is removed from the board, all the overlap references it had will be updated.
	 * @param word
	 */
	private void removeOverlapReferences( Word word ) {
		//System.out.println( "removing overlaps for " + word.getWordString( ) );

		for ( Word w : word.getWordPlacement( ).getOverlaps( ) )
			w.getWordPlacement( ).removeOverlap( word );
	}

	/**
	 * Special case method to place the first word in the center of the given grid.
	 * @param currentWord The first word in the WordList
	 */
	private void placeFirstWord( Word currentWord ) {
		//System.out.println( "Placing first word " + currentWord.getWordString( ) );
		int x = ( width / 2 );
		int y = ( height / 2 );

		Orientation orientation = ( new Random().nextBoolean( ) ? Orientation.HORIZONTAL : Orientation.VERTICAL );

		if ( orientation == Orientation.VERTICAL ) {
			y -= currentWord.getWordString( ).length( );
		}
		else {
			x -= currentWord.getWordString( ).length( );
		}

		currentWord.setWordPlacement( new WordPlacement( new Vector2( x, y ), orientation ) );
		currentWord.setWrittenOnGrid( true );

		writeWordToGrid( currentWord );
		wordsOnGrid.add( currentWord );
	}

	/**
	 * Make sure each word that is on the board is still valid. If any are found to be invalid, the blame is on the most
	 * recently added word, so it will be popped off the grid.
	 */
	private void validateWordsOnBoard() {
		//System.out.println( "Checking all words are valid on board" );

		boolean keepChecking = true;
		int index = 0;
		while ( keepChecking && index < wordsOnGrid.size( ) ) {
			if ( !spacesBeforeAfterClear( wordsOnGrid.get( index ) ) ) {
				popLastWordOffBoard( );
				keepChecking = false;
			}
			index++;
		}
	}

	/**
	 * Remove the last word from the Grid, because it invalidated some other word on the Grid.
	 */
	private void popLastWordOffBoard() {
		deleteWordFromGrid( wordsOnGrid.get( wordsOnGrid.size( ) - 1 ) );
		wordsOnGrid.get( wordsOnGrid.size( ) - 1 ).reset( );
		wordsOnGrid.remove( wordsOnGrid.size( ) - 1 );
	}

	/**
	 * Check the beginning and ending of the given word to make sure there is an empty cell before and after the word.
	 * @param word The word to validate.
	 * @return True is the cells are still clear, False if they are occupied.
	 */
	private boolean spacesBeforeAfterClear( Word word ) {
		//System.out.println( "checking spaces before and after " + word.getWordString( ) );

		Vector2 position = word.getWordPlacement( ).getStartPosition( );
		Orientation orientation = word.getWordPlacement( ).getOrientation( );
		int length = word.getWordString( ).length( );
		boolean stillValid = true;

		if ( orientation == Orientation.HORIZONTAL ) {
			if ( position.getX( ) + length < width && letterGrid[position.getX( ) + length][position.getY( )].getReferences( ).size( ) > 0 )
				stillValid = false;
			if ( position.getX( ) - 1 > 0 && letterGrid[position.getX( ) - 1][position.getY( )].getReferences( ).size( ) > 0 )
				stillValid = false;
		}
		else if ( orientation == Orientation.VERTICAL ) {
			if ( position.getY( ) + length < height && letterGrid[position.getX( )][position.getY( ) + length].getReferences( ).size( ) > 0 )
				stillValid = false;
			if ( position.getY( ) - 1 > 0 && letterGrid[position.getX( )][position.getY( ) - 1].getReferences( ).size( ) > 0 )
				stillValid = false;
		}

		return stillValid;
	}

	/**
	 * DEBUGGING:
	 * Get a String representation of the grid internals.
	 * @return String representation of the grid.
	 */
	@Override
	public String toString() {
		String printedGrid = "";

		for ( int x = 0; x < letterGrid.length; x++ ) {
			for ( int y = 0; y < letterGrid[x].length; y++ )
				printedGrid += letterGrid[x][y];
			printedGrid += "\n";
		}

		return printedGrid;
	}

	/**
	 * DEBUGGING:
	 * Output the grid to a text file.
	 */
	public void writeToFile() {
		try {
			FileOutputStream out = new FileOutputStream( "crossword.txt" );
			out.write( toString( ).getBytes( ) );
			out.close( );
		} catch ( Exception e ) {
			System.out.println( "Failed to write the crossword" );
		}
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
