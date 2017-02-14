package control;

import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

/**
 * PuzzleImageController.java - The Controller that can build other PuzzleImages.  It is set to represent a puzzle
 * using the standard U.S. Paper size of 8.5x11 with a resolution of 150 pixels-per-inch
 *
 * @author Andrew McGuiness
 * @version 08/Feb/2017
 */
//TODO: Fix Comments
public class PuzzleImageController {
	private static final double PAPER_WIDTH = 8.5;
	private static final double PAPER_HEIGHT = 11;
	private static final int PIXELS_PER_INCH = 150;

	private static final int IMAGE_WIDTH = ( int ) ( PAPER_WIDTH * PIXELS_PER_INCH );
	private static final int IMAGE_HEIGHT = ( int ) ( PAPER_HEIGHT * PIXELS_PER_INCH );

	private int width = 0;
	private int height = 0;
	private int cellSize = 0;
	private int yOffset = 0;
	private int xOffset = 0;
	private int buffer = 0;
	private int fontSize = 0;

	private Graphics2D answerKeyWriter = null;
	private Graphics2D blankPuzzleWriter = null;

	/**
	 * Factory Method to create a new puzzle image with the given buffer from the given grid.  The words and
	 * definitions will be pulled from the Words array list inside the grid.
	 *
	 * @param grid   The Grid to use to create the images
	 * @param buffer The buffer is the number of pixels to pad the edge of the image by and the amount of spacing for
	 *               the definitions.
	 */
	public PuzzleImage createPuzzleImage( Grid grid, int buffer ) {
		setDimensions( grid, buffer );

		BufferedImage blankImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );
		BufferedImage answeredImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );

		PuzzleImage puzzleImage = new PuzzleImage( blankImage, answeredImage );

		answerKeyWriter = createGraphics2dWriter( answeredImage );
		blankPuzzleWriter = createGraphics2dWriter( blankImage );


		buildGrid( grid.getLetterGrid( ) );
		writeWordNumbersOnGrid( grid );

		puzzleImage.storeTempFiles( );

		answerKeyWriter = null;
		blankPuzzleWriter = null;
		return puzzleImage;
	}

	/**
	 * Prepare the state of the Object to create the correctly sized Image based on the input grid.
	 *
	 * @param grid   The Grid that the image will be based on.
	 * @param buffer How much of a buffer that should be used when building the image.
	 */
	private void setDimensions( Grid grid, int buffer ) {
		this.buffer = buffer;
		width = ( IMAGE_WIDTH - buffer ) / grid.getWidth( );
		height = ( ( int ) ( IMAGE_HEIGHT * .65 ) - buffer ) / grid.getHeight( );

		cellSize = Math.min( width, height );

		yOffset = buffer / 2;
		xOffset = ( IMAGE_WIDTH - ( cellSize * grid.getWidth( ) ) ) / 2;
		fontSize = cellSize;
	}

	/**
	 * Write the word numbers on the Grid and then Write the corresponding definitions under the grid.
	 *
	 * @param grid The Grid object's WordList and the WordPlacements of those words will be used to correctly write the
	 *             definitions.
	 */
	private void writeWordNumbersOnGrid( Grid grid ) {
		resetGraphics2DWriters( );

		String vertical = "Down: \n";
		String horizontals = "Across: \n";
		int wordNumber = 1;

		for ( Word w : grid.getWordsOnGrid( ) ) {
			Vector2 pos = w.getWordPlacement( ).getStartPosition( );

			answerKeyWriter.drawString( "" + wordNumber, pos.getX( ) * cellSize + xOffset + ( int ) ( cellSize * .1 ), pos.getY( ) * cellSize + yOffset + ( int ) ( cellSize * 0.6 ) );
			blankPuzzleWriter.drawString( "" + wordNumber, pos.getX( ) * cellSize + xOffset + ( int ) ( cellSize * .1 ), pos.getY( ) * cellSize + yOffset + ( int ) ( cellSize * 0.6 ) );

			if ( w.getWordPlacement( ).getOrientation( ) == Orientation.HORIZONTAL )
				horizontals += "\t" + wordNumber + " : " + w.getDefinitionString( ) + "\n";
			else
				vertical += "\t" + wordNumber + " : " + w.getDefinitionString( ) + "\n";

			wordNumber++;
		}

		writeDefinitionsUnderGrid( grid, vertical, horizontals, wordNumber );
	}

	/**
	 * Write the definitions along with their number sorted into the "Across" and "Down" sections
	 *
	 * @param grid        The grid where the words come from.
	 * @param vertical    A string containing all of the definions for the "Down" words
	 * @param horizontals A string containing all the definitions for the "Across" words
	 * @param wordNumber  The total number of words.  Used to calculate a font size to fit all the words into the given
	 *                    area.
	 */
	private void writeDefinitionsUnderGrid( Grid grid, String vertical, String horizontals, int wordNumber ) {
		fontSize = ( int ) ( IMAGE_HEIGHT * .35 ) / ( wordNumber + 4 );

		answerKeyWriter.setFont( new Font( "TimesRoman", Font.PLAIN, fontSize ) );
		blankPuzzleWriter.setFont( new Font( "TimesRoman", Font.PLAIN, fontSize ) );

		int row = 1;
		int initialY = grid.getHeight( ) * cellSize + yOffset + fontSize;

		row = writeStringOfDefinitions( vertical, row, initialY );
		row++;
		writeStringOfDefinitions( horizontals, row, initialY );
	}

	/**
	 * Reset the two Graphics2D objects to write the words numbers on the grid.
	 */
	private void resetGraphics2DWriters( ) {
		answerKeyWriter.setFont( new Font( "TimesRoman", Font.BOLD, ( int ) ( cellSize * .75 ) ) );
		blankPuzzleWriter.setFont( new Font( "TimesRoman", Font.BOLD, ( int ) ( cellSize * .75 ) ) );
		answerKeyWriter.setColor( Color.BLACK );
		blankPuzzleWriter.setColor( Color.BLACK );
	}


	/**
	 * Writes a series of definitions on the board under the grid.  Starting at initial Y, it counts down
	 * "row" rows based on the font size, and begins writing there.
	 *
	 * @param string   All the definitions that need to be written under the grid.
	 * @param row      the row on the page from the initial Y that this method begins at
	 * @param initialY The initial Y for the definition section
	 *
	 * @return the row on the page from the initial Y that this method finished at
	 */
	private int writeStringOfDefinitions( String string, int row, int initialY ) {
		boolean heading = true;
		Scanner stringScanner = new Scanner( string ).useDelimiter( "\n" );
		String def;
		int x, y;

		while ( stringScanner.hasNext( ) ) {
			def = stringScanner.next( );
			x = buffer + ( heading ? 0 : fontSize );
			y = initialY + ( row * fontSize );

			answerKeyWriter.drawString( def, x, y );
			blankPuzzleWriter.drawString( def, x, y );

			if ( heading )
				heading = false;

			row++;
		}
		return row;
	}

	/**
	 * Build the grid itself out of a 2D array of GridCell's.  Both BufferedImages will be written to.  One with just
	 * the boxes, and the other with the boxes and letters.
	 *
	 * @param letterGrid The 2D array of GridCell's that this PuzzleImage is being built from.
	 */
	private void buildGrid( GridCell[][] letterGrid ) {
		for ( int x = 0; x < letterGrid.length; x++ ) {
			for ( int y = 0; y < letterGrid[ x ].length; y++ ) {
				if ( letterGrid[ x ][ y ] != null && letterGrid[ x ][ y ].getCharacter( ) != 0 ) {
					answerKeyWriter.setColor( Color.red );
					answerKeyWriter.drawString( "" + letterGrid[ x ][ y ].getCharacter( ), x * cellSize + xOffset + ( cellSize / 4 ), y * cellSize + yOffset + ( int ) ( cellSize * 0.85 ) );

					drawHollowRect( x, y, Color.black );
				} else if ( letterGrid[ x ][ y ] == null || letterGrid[ x ][ y ].getCharacter( ) == 0 ) {
					drawFilledRect( x, y, Color.black );
				}
			}
		}
	}

	/**
	 * Draw a filled rect on the blank and answered images.
	 *
	 * @param x     The x of the grid.
	 * @param y     The y of the Grid.
	 * @param color What color to use.
	 */
	//TODO: Refactor this
	private void drawFilledRect( int x, int y, Color color ) {
		answerKeyWriter.setColor( color );
		answerKeyWriter.fillRect( x * cellSize + xOffset,
				y * cellSize + yOffset,
				cellSize, cellSize );

		blankPuzzleWriter.setColor( color );
		blankPuzzleWriter.fillRect( x * cellSize + xOffset,
				y * cellSize + yOffset,
				cellSize, cellSize );
	}

	/**
	 * Draw a hollow rect on both images.
	 *
	 * @param x     The starting x for the rect
	 * @param y     The starting y for the rect
	 * @param color The color to use to draw the rect
	 */
	//TODO: refactor this
	private void drawHollowRect( int x, int y, Color color ) {
		answerKeyWriter.setColor( color );
		answerKeyWriter.drawRect( x * cellSize + xOffset,
				y * cellSize + yOffset,
				cellSize, cellSize );

		blankPuzzleWriter.setColor( color );
		blankPuzzleWriter.drawRect( x * cellSize + xOffset,
				y * cellSize + yOffset,
				cellSize, cellSize );
	}

	/**
	 * Create a Graphics2D object to work correctly with the BufferedImage.
	 *
	 * @param img The BufferedImage that this Graphics2D object will be writing to.
	 *
	 * @return The initialized Graphics2D object.
	 */
	private Graphics2D createGraphics2dWriter( BufferedImage img ) {
		Graphics2D g2d = img.createGraphics( );
		g2d.setColor( Color.white );
		g2d.fillRect( 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT );
		g2d.setFont( new Font( "TimesRoman", Font.PLAIN, fontSize ) );
		return g2d;
	}
}