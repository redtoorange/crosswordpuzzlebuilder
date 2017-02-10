package control;

import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 08/Feb/2017
 */
public class CrosswordPuzzleImageController {
	private static final double PAPER_WIDTH = 8.5;
	private static final double PAPER_HEIGHT = 11;
	private static final int PIXELS_PER_INCH = 150;

	private static final int IMAGE_WIDTH = (int)(PAPER_WIDTH * PIXELS_PER_INCH);
	private static final int IMAGE_HEIGHT = (int)(PAPER_HEIGHT * PIXELS_PER_INCH);

	private int width = 0;
	private int height = 0;
	private int cellSize = 0;
	private int yOffset = 0;
	private int xOffset = 0;
	private int buffer = 0;
	private int fontSize = 0;

	private Graphics2D answerKeyWriter = null;
	private Graphics2D blankPuzzleWriter = null;


	private CrosswordPuzzleImage puzzleImage;

	public void createPuzzleImage( Grid grid, int buffer ){
		GridCell[][] letterGrid = grid.getLetterGrid();

		setDimensions( grid, buffer );

		BufferedImage blankImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );
		BufferedImage answeredImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );
		puzzleImage = new CrosswordPuzzleImage( blankImage, answeredImage );

		answerKeyWriter = initGraphics( answeredImage );
		blankPuzzleWriter = initGraphics( blankImage );


		buildGrid( grid, letterGrid );
		writeWordNumbers( grid );

		puzzleImage.storeTempFiles();
	}

	private void setDimensions( Grid grid, int buffer ) {
		this.buffer = buffer;
		width = (IMAGE_WIDTH - buffer ) / grid.getWidth();
		height = ( (int)(IMAGE_HEIGHT * .65) - buffer) / grid.getHeight();

		cellSize = Math.min( width, height );

		yOffset = buffer/2;
		xOffset = (IMAGE_WIDTH - ( cellSize * grid.getWidth( )) ) / 2;
		fontSize = cellSize;
	}

	private void writeWordNumbers( Grid grid ) {
		resetGraphics2D( );

		String vertical = "Down: \n";
		String horizontals = "Across: \n";
		int wordNumber = 1;

		for( Word w : grid.getWordsOnGrid()){
			Vector2 pos = w.getWordPlacement().getStartPosition();

			answerKeyWriter.drawString( "" + wordNumber,  pos.getX() * cellSize + xOffset + (int)(cellSize*.1), pos.getY() * cellSize + yOffset + (int)(cellSize * 0.6)  );
			blankPuzzleWriter.drawString( "" + wordNumber,  pos.getX() * cellSize + xOffset + (int)(cellSize*.1), pos.getY() * cellSize + yOffset + (int)(cellSize * 0.6)  );

			if(w.getWordPlacement().getOrientation() == Orientation.HORIZONTAL)
				horizontals += "\t" + wordNumber + " : " + w.getDefinitionString() + "\n";
			else{
				vertical += "\t" + wordNumber + " : " + w.getDefinitionString() + "\n";
			}

			wordNumber++;
		}

		writeDefintions( grid, vertical, horizontals, wordNumber );
	}

	private void resetGraphics2D( ) {
		answerKeyWriter.setFont( new Font("TimesRoman", Font.BOLD, (int)(cellSize * .75) ) );
		blankPuzzleWriter.setFont( new Font("TimesRoman", Font.BOLD, (int)(cellSize * .75) ) );
		answerKeyWriter.setColor( Color.BLACK );
		blankPuzzleWriter.setColor( Color.BLACK );
	}

	private void writeDefintions( Grid grid, String vertical, String horizontals, int wordNumber ) {
		fontSize = (int)(IMAGE_HEIGHT * .35) / (wordNumber + 4);

		answerKeyWriter.setFont( new Font("TimesRoman", Font.PLAIN, fontSize ) );
		blankPuzzleWriter.setFont( new Font("TimesRoman", Font.PLAIN, fontSize ) );


		int row = 1;
		int initialY = grid.getHeight() * cellSize + yOffset + fontSize;

		row = writeDefinitionString( vertical, row, initialY );
		row++;
		writeDefinitionString( horizontals, row, initialY );
	}

	private int writeDefinitionString( String string, int row, int initialY ) {
		boolean heading = true;
		Scanner stringScanner = new Scanner( string ).useDelimiter( "\n" );
		String def;
		int x, y;

		while(stringScanner.hasNext()){
			def = stringScanner.next();
			x = buffer + (heading ? 0 : fontSize);
			y = initialY + (row * fontSize );

			answerKeyWriter.drawString( def, x, y );
			blankPuzzleWriter.drawString( def, x, y );

			if(heading)
				heading = false;

			row++;
		}
		return row;
	}

	private void buildGrid( Grid grid, GridCell[][] letterGrid ) {
		for(int x = 0; x < grid.getWidth(); x++){
			for(int y = 0; y < grid.getHeight(); y++){
				if( letterGrid[x][y] != null){
					if( letterGrid[x][y].getCharacter(  ) == 0 ){
						answerKeyWriter.setColor( Color.black );
						answerKeyWriter.fillRect( 	x * cellSize + xOffset,
								y * cellSize + yOffset,
								cellSize, cellSize );

						blankPuzzleWriter.setColor( Color.black );
						blankPuzzleWriter.fillRect( 	x * cellSize + xOffset,
								y * cellSize + yOffset,
								cellSize, cellSize );
					}
					else {
						answerKeyWriter.setColor( Color.black );
						answerKeyWriter.drawRect( x * cellSize + xOffset,
								y * cellSize + yOffset,
								cellSize, cellSize );
						answerKeyWriter.setColor( Color.red );
						answerKeyWriter.drawString( "" + letterGrid[x][y].getCharacter(),  x * cellSize + xOffset + (cellSize/4),y * cellSize + yOffset + (int)(cellSize * 0.85)  );

						blankPuzzleWriter.setColor( Color.black );
						blankPuzzleWriter.drawRect( x * cellSize + xOffset,
								y * cellSize + yOffset,
								cellSize, cellSize );
					}
				}
				else{
					answerKeyWriter.setColor( Color.black );
					answerKeyWriter.fillRect( 	x * cellSize + xOffset,
							y * cellSize + yOffset,
							cellSize, cellSize );

					blankPuzzleWriter.setColor( Color.black );
					blankPuzzleWriter.fillRect( 	x * cellSize + xOffset,
							y * cellSize + yOffset,
							cellSize, cellSize );
				}
			}
		}
	}

	private Graphics2D initGraphics( BufferedImage img ) {
		Graphics2D g2d = img.createGraphics();
		g2d.setColor( Color.white );
		g2d.fillRect( 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT );
		g2d.setFont( new Font( "TimesRoman", Font.PLAIN, fontSize ) );
		return g2d;
	}

	public void cleanup(){
		if(puzzleImage != null){
			if(puzzleImage.getAnsweredImageFile( ) != null)
				puzzleImage.getAnsweredImageFile( ).delete( );
			if(puzzleImage.getBlankImageFile( ) != null)
				puzzleImage.getBlankImageFile( ).delete( );
		}
	}

	public CrosswordPuzzleImage getPuzzleImage( ) {
		return puzzleImage;
	}
}
