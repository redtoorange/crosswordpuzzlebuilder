package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * CrosswordPuzzleImage.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
public class CrosswordPuzzleImage {
	public static final double PAPER_WIDTH = 8.5;
	public static final double PAPER_HEIGHT = 11;
	public static final int PIXELS_PER_INCH = 72;

	public static final int IMAGE_WIDTH = (int)(PAPER_WIDTH * PIXELS_PER_INCH);
	public static final int IMAGE_HEIGHT = (int)(PAPER_HEIGHT * PIXELS_PER_INCH);

	private BufferedImage bufferedImage;

	public CrosswordPuzzleImage( Grid grid, int buffer ) {
		GridCell[][] letterGrid = grid.getLetterGrid();

		int width = (IMAGE_WIDTH - buffer ) / grid.getWidth();
		int height = (IMAGE_HEIGHT - buffer) / grid.getHeight();

		int cellSize = Math.min( width, height );

		int yOffset = (IMAGE_HEIGHT - ( cellSize * grid.getHeight()) ) / 2;
		int xOffset = (IMAGE_WIDTH - ( cellSize * grid.getWidth( )) ) / 2;


		bufferedImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setColor( Color.white );
		g2d.fillRect( 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT );


		g2d.setColor( Color.GREEN );



		g2d.setFont( new Font("TimesRoman", Font.PLAIN, cellSize) );

		for(int x = 0; x < grid.getWidth(); x++){
			for(int y = 0; y < grid.getHeight(); y++){
				if( letterGrid[x][y] != null){
					if( letterGrid[x][y].getCharacter(  ) == 0 ){
					g2d.setColor( Color.black );
					g2d.fillRect( 	x * cellSize + xOffset,
							y * cellSize + yOffset,
							cellSize, cellSize );
					}
					else {
						g2d.setColor( Color.black );
						g2d.drawRect( x * cellSize + xOffset,
								y * cellSize + yOffset,
								cellSize, cellSize );
						g2d.setColor( Color.red );
						g2d.drawString( "" + letterGrid[x][y].getCharacter(),  x * cellSize + xOffset + (cellSize/4),y * cellSize + yOffset + (int)(cellSize * 0.85)  );
					}
				}
				else{
					g2d.setColor( Color.black );
					g2d.fillRect( 	x * cellSize + xOffset,
							y * cellSize + yOffset,
							cellSize, cellSize );
				}
			}
		}

		g2d.setFont( new Font("TimesRoman", Font.BOLD, (int)(cellSize * .75) ) );
		int wordNumber = 1;
		for(Word w : grid.getWordsOnGrid()){
			Vector2 pos = w.getWordPlacement().getStartPosition();
			g2d.setColor( Color.BLACK );
			g2d.drawString( "" + wordNumber,  pos.getX() * cellSize + xOffset + (int)(cellSize*.1), pos.getY() * cellSize + yOffset + (int)(cellSize * 0.6)  );
			wordNumber++;
		}
	}

	public File writeImageToFile( String fileName ) {
		File image = new File( fileName );
		try {
			ImageIO.write( bufferedImage, "png", image );
			System.out.println( "Job's a gud'un." );
		}
		catch( IOException e){
			System.out.println( "Fatal error writing image: " + e.getMessage() );
			e.printStackTrace();
		}
		return image;
	}
}