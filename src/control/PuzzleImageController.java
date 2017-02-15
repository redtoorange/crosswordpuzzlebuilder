package control;

import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

/**
 * PuzzleImageController.java - The Controller that can build {@link PuzzleImage}s.  It is set to represent a puzzle
 * using the standard U.S. Paper size of 8.5x11 with a resolution of 150 pixels-per-inch
 *
 * @author Andrew McGuiness
 * @version 14/Feb/2017
 */
//TODO: Fix Comments
public class PuzzleImageController {
	/**
	 * Standard Width in inches of U.S. Printer Paper.
	 */
	private static final double PAPER_WIDTH = 8.5;
	/**
	 * Standard Height in inches of U.S. Printer Paper.
	 */
	private static final double PAPER_HEIGHT = 11;
	/**
	 * The resolution of the final image in pixels per square inch.
	 */
	private static final int PIXELS_PER_INCH = 150;
	/**
	 * The image Width in pixels, based on the PPI.
	 */
	private static final int IMAGE_WIDTH = ( int ) ( PAPER_WIDTH * PIXELS_PER_INCH );
	/**
	 * The image Height in pixels, based on the PPI.
	 */
	private static final int IMAGE_HEIGHT = ( int ) ( PAPER_HEIGHT * PIXELS_PER_INCH );

	/**
	 * Set based on the height, width and buffer passed in when building the {@link PuzzleImage}.  Determine where the
	 * rects will be draw on the output image.
	 */
	private int width, height, cellSize, yOffset, xOffset, buffer, fontSize;

	/**
	 * The {@link Graphics2D} used to write to the answered image of the {@link PuzzleImage}.
	 */
	private Graphics2D answerKeyWriter;
	/**
	 * The {@link Graphics2D} used to write to the blank image of the {@link PuzzleImage}.
	 */
	private Graphics2D blankPuzzleWriter;

	/**
	 * Factory Method to create a new {@link PuzzleImage} with the given buffer amount from the given {@link Grid}.
	 * The words and definitions will be pulled from the {@link Word} {@link java.util.ArrayList} inside the {@link Grid}.
	 *
	 * @param grid   The {@link Grid} to use to create the {@link BufferedImage}s.
	 * @param buffer The buffer is the number of pixels to pad the edge of the {@link BufferedImage}s by and the amount
	 *               of spacing for the definitions at the bottom.
	 */
	public PuzzleImage createPuzzleImage( Grid grid, int buffer ) {
		setDimensions( grid, buffer );

		PuzzleImage puzzleImage = initPuzzleImage( );

		buildGrid( grid.getLetterGrid( ) );
		writeWordNumbersOnGrid( grid );
		puzzleImage.storeTempFiles( );

		return puzzleImage;
	}

	/**
	 * Initialize a new {@link PuzzleImage} and it's {@link BufferedImage}s to be written to.  The {@link Graphics2D}
	 * references are pulled and they are stored.
	 *
	 * @return The initialized {@link PuzzleImage}.
	 */
	private PuzzleImage initPuzzleImage() {
		BufferedImage blankImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );
		BufferedImage answeredImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );

		PuzzleImage puzzleImage = new PuzzleImage( blankImage, answeredImage );

		answerKeyWriter = createGraphics2dWriter( answeredImage );
		blankPuzzleWriter = createGraphics2dWriter( blankImage );

		return puzzleImage;
	}

	/**
	 * Prepare the dimension variables based on the input {@link Grid} and buffer amount.
	 *
	 * @param grid   The {@link Grid} that the {@link PuzzleImage} will be based on.
	 * @param buffer How much of a buffer that should be used when building the {@link PuzzleImage}.
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
	 * Write the word numbers on the  {@link Grid}, then write the corresponding definitions under the  {@link Grid}.
	 *
	 * @param grid The  {@link Grid}'s {@link Word}s and the {@link WordPlacement} of those {@link Word}s will be used
	 *             to correctly write the definitions.
	 */
	private void writeWordNumbersOnGrid( Grid grid ) {
		resetGraphics2DWriters( );

		String vertical = "Down: \n";
		String horizontals = "Across: \n";
		int wordNumber = 1;

		for ( Word word : grid.getWordsOnGrid( ) ) {
			Vector2 pos = word.getWordPlacement( ).getStartPosition( );

			int x = pos.getX( ) * cellSize + xOffset + ( int ) ( cellSize * .1 );
			int y = pos.getY( ) * cellSize + yOffset + ( int ) ( cellSize * 0.6 );

			answerKeyWriter.drawString( "" + wordNumber, x, y );
			blankPuzzleWriter.drawString( "" + wordNumber, x, y );

			if ( word.getWordPlacement( ).getOrientation( ) == Orientation.HORIZONTAL )
				horizontals += "\t" + wordNumber + " : " + word.getDefinitionString( ) + "\n";
			else
				vertical += "\t" + wordNumber + " : " + word.getDefinitionString( ) + "\n";

			wordNumber++;
		}

		writeDefinitionsUnderGrid( grid, vertical, horizontals, wordNumber );
	}

	/**
	 * Write the definitions along with their number sorted into the "Across" and "Down" sections
	 *
	 * @param grid        The {@link Grid} where the {@link Word}s come from.
	 * @param vertical    A {@link String} containing all of the definions for the "Down" words
	 * @param horizontals A {@link String} containing all the definitions for the "Across" words
	 * @param wordNumber  The total number of {@link Word}s.  Used to calculate a font size to fit all the {@link Word}
	 *                    into the given area.
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
	 * Reset the two {@link Graphics2D} objects to write the {@link Word} numbers on the {@link BufferedImage} grid.
	 */
	private void resetGraphics2DWriters() {
		answerKeyWriter.setFont( new Font( "TimesRoman", Font.BOLD, ( int ) ( cellSize * .75 ) ) );
		blankPuzzleWriter.setFont( new Font( "TimesRoman", Font.BOLD, ( int ) ( cellSize * .75 ) ) );
		answerKeyWriter.setColor( Color.BLACK );
		blankPuzzleWriter.setColor( Color.BLACK );
	}


	/**
	 * Writes a series of definitions on the {@link BufferedImage} under the {@link Grid}.  Starting at initial Y, it
	 * counts down "row"-many rows based on the font size, and begins writing there.
	 *
	 * @param string   All the definitions that need to be written under the {@link BufferedImage} grid.
	 * @param row      the row on the page from the initial Y that this method begins at
	 * @param initialY The initial Y for the definition section
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
	 * Build the grid itself out of a 2D array of {@link GridCell}s.  Both {@link BufferedImage}s will be written to.
	 * One with just the boxes and the other with the boxes and letters.
	 *
	 * @param letterGrid The 2D array of GridCell's that this PuzzleImage is being built from.
	 */
	private void buildGrid( GridCell[][] letterGrid ) {
		for ( int x = 0; x < letterGrid.length; x++ ) {
			for ( int y = 0; y < letterGrid[x].length; y++ ) {
				if ( letterGrid[x][y] != null && letterGrid[x][y].getCharacter( ) != 0 ) {
					answerKeyWriter.setColor( Color.red );
					answerKeyWriter.drawString( "" + letterGrid[x][y].getCharacter( ), x * cellSize + xOffset + ( cellSize / 4 ), y * cellSize + yOffset + ( int ) ( cellSize * 0.85 ) );

					drawHollowRect( x, y, Color.black );
				}
				else if ( letterGrid[x][y] == null || letterGrid[x][y].getCharacter( ) == 0 ) {
					drawFilledRect( x, y, Color.black );
				}
			}
		}
	}

	/**
	 * Draw a filled rect on the blank and answered {@link BufferedImage}s.
	 *
	 * @param x     The x of the rect in the original {@link Grid}.
	 * @param y     The y of the rect in the original {@link Grid}.
	 * @param color What {@link Color} to use for the rect.
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
	 * Draw a hollow rect on both {@link BufferedImage}s.
	 *
	 * @param x     The x of the rect in the original {@link Grid}.
	 * @param y     The y of the rect in the original {@link Grid}.
	 * @param color What {@link Color} to use for the rect.
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
	 * Create a {@link Graphics2D} object to work correctly with the {@link BufferedImage}.
	 *
	 * @param image The {@link BufferedImage} that this {@link Graphics2D} object will be writing to.
	 * @return The initialized {@link Graphics2D} object.
	 */
	private Graphics2D createGraphics2dWriter( BufferedImage image ) {
		Graphics2D g2d = image.createGraphics( );
		g2d.setColor( Color.white );
		g2d.fillRect( 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT );
		g2d.setFont( new Font( "TimesRoman", Font.PLAIN, fontSize ) );
		return g2d;
	}
}