package control;

import model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * GridController.java - Responsible for building the components that create a {@link Grid}.
 * It then initializes the {@link Grid} and returns it.
 *
 * @author Andrew McGuiness
 * @version 15/Feb/2017
 * @see Grid
 * @see GridCell
 * @see WordList
 * @see WordPlacement
 * @see Word
 */
public class GridController {
	/** The arbitrary width chosen to start the {@link GridCell} at.  Allows for enough room to expand. */
	private static int STARTING_WIDTH = 100;

	/** The arbitrary height chosen to start the {@link GridCell} at.  Allows for enough room to expand. */
	private static int STARTING_HEIGHT = 100;

	/** The 2D array of {@link GridCell}s that represents the crossword puzzle board. */
	private GridCell[][] letterGrid;

	/** The {@link ArrayList} containing all the {@link Word}s that are currently written on the {@link GridCell} array. */
	private ArrayList< Word > wordsOnGrid;

	/** The Dimension of the {@link GridCell} array. */
	private int width, height;


	/**
	 * Essentially a factory method to create a {@link Grid} from a given {@link DictionaryFile}
	 *
	 * @param dictionaryFile The {@link DictionaryFile} to pull the {@link WordList} from.
	 * @return The finished {@link Grid} object.
	 */
	public Grid createGrid( DictionaryFile dictionaryFile ) {
		wordsOnGrid = new ArrayList< Word >( );
		letterGrid = new GridCell[STARTING_WIDTH][STARTING_HEIGHT];
		width = STARTING_WIDTH;
		height = STARTING_HEIGHT;

		initGridCells( );
		buildPuzzle( dictionaryFile );

		return new Grid( width, height, letterGrid, wordsOnGrid );
	}

	/**
	 * Initialize all the {@link GridCell} references in the 2D letterGrid array.
	 */
	private void initGridCells() {
		for ( int x = 0; x < letterGrid.length; x++ )
			for ( int y = 0; y < letterGrid[x].length; y++ )
				letterGrid[x][y] = new GridCell( );
	}

	/**
	 * Reset each {@link GridCell} in the 2d letterGrid array.  Used when the {@link GridCell} needs to be
	 * reset for a new iteration.
	 */
	private void resetGridCells() {
		for ( int x = 0; x < letterGrid.length; x++ )
			for ( int y = 0; y < letterGrid[x].length; y++ )
				letterGrid[x][y].reset( );
	}

	/**
	 * Fit the {@link Word}s from the {@link DictionaryFile}'s {@link WordList} onto the {@link GridCell} array.
	 *
	 * @param dictionaryFile the {@link DictionaryFile} to be use for the {@link WordList}.
	 */
	private void buildPuzzle( DictionaryFile dictionaryFile ) {
		WordList words = dictionaryFile.getWordList( );

		//Loop until the board is valid
		boolean validBoard = false;
		while ( !validBoard ) {
			validBoard = attemptToPlaceAllWords( words );

			if ( !validBoard ) {
				clearTheGrid( );
				dictionaryFile.reset( );
			}
		}

		fitGridToCrossWord( );
	}

	/**
	 * A soft-reset of the {@link GridCell} array that doesn't manipulate the letterGrid Array dimensions.
	 * Used when the grid is found to be invalid.
	 */
	private void clearTheGrid() {
		resetGridCells( );
		wordsOnGrid.clear( );
	}

	/**
	 * Attempt to find a spot for all the {@link Word}s in the {@link WordList} of the {@link DictionaryFile}.
	 *
	 * @param words The {@link WordList} to be placed on the {@link GridCell} grid.
	 * @return True of all the {@link Word}s in the {@link WordList} were placed on the {@link GridCell} grid.
	 */
	private boolean attemptToPlaceAllWords( WordList words ) {
		Word currentWord = words.getNextUnplaced( );
		int iterations = words.listSize( ) * words.listSize( );

		while ( currentWord != null && iterations > 0 ) {
			if ( attemptToPlaceWord( currentWord ) ) {
				if ( wordsOnGrid.size( ) > 1 ) {
					if ( !validateWordsOnBoard( ) ) {
						words.pushToBack( currentWord );
					}
				}
			}
			else
				words.pushToBack( currentWord );

			currentWord = words.getNextUnplaced( );
			iterations--;
		}

		return wordsOnGrid.size( ) == words.listSize( );
	}

	/**
	 * Resize the 2D {@link GridCell} array to fit around the elements on the {@link Grid}.
	 * Basically clears out empty columns and rows from the {@link GridCell} array.
	 */
	private void fitGridToCrossWord() {
		BoundingBox box = new BoundingBox( );

		calculateExtents( box );
		recalculateWordPlacements( box );
		adjustGridDimensions( box );

		letterGrid = copyToNewCellGrid( box );
	}

	/**
	 * Calculate the min and max x and y of the {@link GridCell} array, storing them into a
	 * {@link BoundingBox} for use.
	 *
	 * @param box The {@link BoundingBox} to store the extents in.
	 */
	private void calculateExtents( BoundingBox box ) {
		for ( int x = 0; x < letterGrid.length; x++ )
			for ( int y = 0; y < letterGrid[x].length; y++ )
				if ( letterGrid[x][y].getReferences( ).size( ) > 0 )
					boundsCheck( box, x, y );
	}

	/**
	 * Check an x and a y against the contents of a {@link BoundingBox} to see if they are significant for
	 * dimension calculation.
	 *
	 * @param box The {@link BoundingBox} that should contain the extents of the {@link GridCell} array.
	 * @param x   The x in the {@link GridCell} array to check.
	 * @param y   The y in the {@link GridCell} array to check.
	 */
	private void boundsCheck( BoundingBox box, int x, int y ) {
		if ( y < box.getMinY( ) )
			box.setMinY( y );
		if ( y > box.getMaxY( ) )
			box.setMaxY( y );
		if ( x < box.getMinX( ) )
			box.setMinX( x );
		if ( x > box.getMaxX( ) )
			box.setMaxX( x );
	}


	/**
	 * Based on the new {@link BoundingBox}, all of the {@link Vector2} starting positions need to be updated.
	 *
	 * @param box The {@link BoundingBox} that contains the extents of the {@link GridCell} array.
	 */
	private void recalculateWordPlacements( BoundingBox box ) {
		for ( Word w : wordsOnGrid ) {
			Vector2 pos = w.getWordPlacement( ).getStartPosition( );

			pos.setX( pos.getX( ) - box.getMinX( ) + 1 );
			pos.setY( pos.getY( ) - box.getMinY( ) + 1 );
		}
	}

	/**
	 * Adjust the height and the width of this {@link Grid} in regards to the new {@link GridCell} array dimensions and
	 * extents.
	 *
	 * @param box The {@link BoundingBox} that contains the extents of the {@link GridCell} array.
	 */
	private void adjustGridDimensions( BoundingBox box ) {
		width = box.getMaxX( ) - box.getMinX( ) + 3;
		height = box.getMaxY( ) - box.getMinY( ) + 3;
	}

	/**
	 * Copy the contents of the default {@link GridCell} array into a new, resized {@link GridCell} array.
	 *
	 * @param box The {@link BoundingBox} that contains the extents of the {@link GridCell} array.
	 * @return A new 2D {@link GridCell} array that has all the referenced {@link GridCell}s copied.
	 */
	private GridCell[][] copyToNewCellGrid( BoundingBox box ) {
		GridCell[][] newGrid = new GridCell[width][height];

		for ( int x = 1; x < width - 1; x++ )
			for ( int y = 1; y < height - 1; y++ )
				newGrid[x][y] = letterGrid[x + box.getMinX( ) - 1][y + box.getMinY( ) - 1];

		return newGrid;
	}

	/**
	 * Try to place the given {@link Word} on the {@link GridCell} array.
	 *
	 * @param currentWord Which {@link Word} to try to find a valid {@link WordPlacement} for.
	 * @return True if the {@link Word} was successfully placed.
	 */
	private boolean attemptToPlaceWord( Word currentWord ) {
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
	 * Find all letters in all {@link Word}s currently on the {@link GridCell} array that have a matching
	 * letter to the current {@link Word}.  Each match will be encapsulated into a {@link WordPlacement}
	 * Object that represents a potential overlap location.
	 *
	 * @param current The {@link Word} representation of the current {@link Word}.
	 * @return An {@link ArrayList} of {@link WordPlacement}s that represents all the overlaps that are suitable.
	 */
	private ArrayList< WordPlacement > findLetterMatches( String current ) {
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
	 * Parse the {@link ArrayList} of letter matching {@link WordPlacement}s to create a list of
	 * valid {@link WordPlacement}s that cause overlaps.
	 *
	 * @param potentialOverlaps A {@link ArrayList} of all the overlaps detected.
	 * @param current           The {@link String} representation of the current {@link Word}.
	 * @return An {@link ArrayList} of all the valid {@link WordPlacement}s that have been generated that would cause
	 * overlaps.
	 */
	private ArrayList< WordPlacement > generatePlacements( ArrayList< WordPlacement > potentialOverlaps, String current ) {
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
	 * Test a {@link WordPlacement} for validity.  If valid, it will be returned an added as a candidate
	 * {@link WordPlacement}.
	 *
	 * @param current     The {@link String} representation of the current {@link Word}.
	 * @param orientation The {@link Orientation} to test.
	 * @param startX      The starting X position to test.
	 * @param startY      The starting Y position to test.
	 * @return Null if invalid, otherwise the {@link WordPlacement} was determined to be valid.
	 */
	private WordPlacement testPlacement( String current, Orientation orientation, final int startX, final int startY ) {
		WordPlacement placement = null;
		boolean withinBounds = isWithinBounds( orientation, startX, startY, current.length( ) );

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
	 * Check the beginning and ending of a {@link WordPlacement} to make sure there is an empty {@link GridCell}.
	 *
	 * @param current     The {@link String} representation of the current {@link Word}.
	 * @param orientation What {@link Orientation} that should be tested for conflicts
	 * @param startX      The starting X position
	 * @param startY      The starting Y position
	 * @return False if there are no conflicts.
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
	 * Step through each potential {@link GridCell} that the {@link Word} could occupy.  If the {@link GridCell}
	 * is empty, continue, if not, test the {@link GridCell}'s char to see if it matches
	 * the index of the current word {@link String}. If it does, it is a valid {@link WordPlacement} overlap.  So it
	 * parses the {@link GridCell}'s {@link Word} references and adds them to the potential overlaps {@link ArrayList}.
	 *
	 * @param current          The {@link String} representation of the current {@link Word}.
	 * @param orientation      The {@link Orientation} of the {@link Word} to test.
	 * @param startX           The starting X position to test.
	 * @param startY           The starting Y position to test.
	 * @param possibleOverlaps An {@link ArrayList} that will contain all {@link Word} references that the tested {@link
	 *                         WordPlacement} overlaps.
	 * @return False is there were no conflicts.  True if a {@link GridCell} in the path did not match the word.
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
	 * Get a list of references from a {@link GridCell} at a specific {@link Vector2} position.
	 *
	 * @param orientation The {@link Orientation} that needs to be validated.
	 * @param x           The starting x position to be tested.
	 * @param y           The starting y position to be tested.
	 * @param offset      How much to offset the position based on the {@link Orientation}.
	 * @return A list of references to all the {@link Word}s that overlap the given {@link GridCell}.
	 */
	private ArrayList< Word > getReferences( Orientation orientation, final int x, final int y, final int offset ) {
		return orientation == Orientation.HORIZONTAL ?
				letterGrid[x + offset][y].getReferences( ) :
				letterGrid[x][y + offset].getReferences( );
	}

	/**
	 * Get a char from a {@link GridCell} at a specified {@link Vector2} position.
	 *
	 * @param orientation The {@link Orientation} that needs to be validated.
	 * @param x           The starting x position to be tested.
	 * @param y           The starting y position to be tested.
	 * @param offset      How much to offset the position based on the {@link Orientation}.
	 * @return the char that the {@link GridCell} contains.
	 */
	private char getChar( Orientation orientation, final int x, final int y, final int offset ) {
		return orientation == Orientation.HORIZONTAL ?
				letterGrid[x + offset][y].getCharacter( ) :
				letterGrid[x][y + offset].getCharacter( );
	}

	/**
	 * Ensure that a {@link WordPlacement} will be within the bounds of the {@link GridCell} array.
	 *
	 * @param orientation The {@link Orientation} that needs to be validated.
	 * @param x           The starting x position to be tested.
	 * @param y           The starting y position to be tested.
	 * @param wordLength  The length of the word that is being tested.
	 * @return True is the dimensions are within the bounds of the {@link GridCell} array.
	 */
	private boolean isWithinBounds( Orientation orientation, final int x, final int y, final int wordLength ) {
		boolean withinBounds = false;

		if ( orientation == Orientation.HORIZONTAL )
			withinBounds = ( x >= 0 && x + wordLength < width && y >= 0 && y < height );
		else if ( orientation == Orientation.VERTICAL )
			withinBounds = ( x >= 0 && x < width && y >= 0 && y + wordLength < height );

		return withinBounds;
	}

	/**
	 * Parse the list of all valid {@link WordPlacement}s and determine the one with the most overlaps.
	 * This one will be used to place the {@link Word}.
	 *
	 * @param placements List of valid {@link WordPlacement}s on the {@link GridCell} array.
	 * @return The best {@link WordPlacement} determined by number of overlaps.
	 */
	private WordPlacement findBestPlacement( ArrayList< WordPlacement > placements ) {
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
	 * Write a {@link Word} onto the {@link GridCell} array, populating {@link GridCell}s with references and a
	 * chars if they are empty.
	 *
	 * @param currentWord The {@link Word} to write to the {@link GridCell} array.
	 */
	private void writeWordToGrid( Word currentWord ) {
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
	 * Update all the {@link Word}s that the currentWord overlaps with on the {@link GridCell} array.
	 *
	 * @param currentWord The {@link Word} to add to other {@link WordPlacement} overlaps.
	 */
	private void addOverlapReferences( Word currentWord ) {
		for ( Word overlappingWord : currentWord.getWordPlacement( ).getOverlaps( ) )
			overlappingWord.getWordPlacement( ).addOverlap( currentWord );
	}

	/**
	 * Remove a {@link Word} from the {@link GridCell} array, this effectively clears
	 * the cells if they no longer have any {@link Word} references.
	 *
	 * @param currentWord The {@link Word} to deleted from the {@link GridCell} array.
	 */
	private void deleteWordFromGrid( Word currentWord ) {
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
	 * When a {@link Word} is removed from the {@link GridCell} array, all the overlap references
	 * it had will be updated.
	 *
	 * @param word {@link Word} to remove from all {@link WordPlacement} overlaps.
	 */
	private void removeOverlapReferences( Word word ) {
		for ( Word w : word.getWordPlacement( ).getOverlaps( ) )
			w.getWordPlacement( ).removeOverlap( word );
	}

	/**
	 * Special case method to place the first {@link Word} in the center of the {@link GridCell} array.
	 *
	 * @param currentWord The first {@link Word} in the {@link WordList}.
	 */
	private void placeFirstWord( Word currentWord ) {
		int x = ( width / 2 );
		int y = ( height / 2 );

		Orientation orientation = ( new Random( ).nextBoolean( ) ? Orientation.HORIZONTAL : Orientation.VERTICAL );

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
	 * Make sure each {@link Word} that is on {@link GridCell} array still valid. If any are
	 * found to be invalid, the blame is on the most recently added {@link Word}, so it will
	 * be popped off the {@link GridCell} array.
	 *
	 * @return True if all {@link Word}s in the wordsOnGrid {@link ArrayList} are still valid.
	 */
	private boolean validateWordsOnBoard() {
		boolean keepChecking = true;
		int index = 0;
		while ( keepChecking && index < wordsOnGrid.size( ) ) {
			if ( !spacesBeforeAfterClear( wordsOnGrid.get( index ) ) ) {
				popLastWordOffBoard( );
				keepChecking = false;
			}
			index++;
		}
		return keepChecking;
	}

	/**
	 * Remove the last {@link Word} from the wordsOnGrid {@link ArrayList} and {@link GridCell} array, because it
	 * invalidated some other {@link Word} on the {@link GridCell} array.
	 */
	private void popLastWordOffBoard() {
		deleteWordFromGrid( wordsOnGrid.get( wordsOnGrid.size( ) - 1 ) );
		wordsOnGrid.remove( wordsOnGrid.size( ) - 1 );
	}

	/**
	 * Check the beginning and ending of the given {@link Word} to make sure there is an empty {@link GridCell}
	 * before and after the {@link Word}.
	 *
	 * @param word The {@link Word} to validate.
	 * @return True if the {@link GridCell} are still clear, False if they are occupied.
	 */
	private boolean spacesBeforeAfterClear( Word word ) {
		Vector2 position = word.getWordPlacement( ).getStartPosition( );
		Orientation orientation = word.getWordPlacement( ).getOrientation( );
		int length = word.getWordString( ).length( );
		boolean stillValid = true;

		if ( orientation == Orientation.HORIZONTAL )
			stillValid = stillValidHorizontal( position, length );
		else if ( orientation == Orientation.VERTICAL )
			stillValid = stillValidVertical( position, length );

		return stillValid;
	}

	/**
	 * Test the front and back of a {@link Orientation} Vertical {@link Word}.
	 *
	 * @param position The starting {@link Vector2} position of the {@link Word}.
	 * @param length   The length of the {@link Word}
	 * @return False if the {@link GridCell} in-front or behind the {@link Word} is occupied.
	 */
	private boolean stillValidVertical( Vector2 position, int length ) {
		boolean stillValid = true;

		if ( position.getY( ) + length < height && letterGrid[position.getX( )][position.getY( ) + length].getReferences( ).size( ) > 0 )
			stillValid = false;
		if ( position.getY( ) - 1 > 0 && letterGrid[position.getX( )][position.getY( ) - 1].getReferences( ).size( ) > 0 )
			stillValid = false;

		return stillValid;
	}

	/**
	 * Test the front and back of a {@link Orientation} Horizontal {@link Word}.
	 *
	 * @param position The starting position of the {@link Word}.
	 * @param length   The length of the {@link Word}
	 * @return False if the {@link GridCell} in-front or behind the {@link Word} is occupied.
	 */
	private boolean stillValidHorizontal( Vector2 position, int length ) {
		boolean stillValid = true;

		if ( position.getX( ) + length < width && letterGrid[position.getX( ) + length][position.getY( )].getReferences( ).size( ) > 0 )
			stillValid = false;
		if ( position.getX( ) - 1 > 0 && letterGrid[position.getX( ) - 1][position.getY( )].getReferences( ).size( ) > 0 )
			stillValid = false;

		return stillValid;
	}
}