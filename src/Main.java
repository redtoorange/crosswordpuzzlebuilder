import view.DictionaryLoaderView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class Main {

	public static final double PAPER_WIDTH = 8.5;
	public static final double PAPER_HEIGHT = 11;
	public static final int PIXELS_PER_INCH = 72;


	public static final int IMAGE_WIDTH = (int)(PAPER_WIDTH * PIXELS_PER_INCH);
	public static final int IMAGE_HEIGHT = (int)(PAPER_HEIGHT * PIXELS_PER_INCH);

	public static final int COLUMNS = 25;
	public static final int ROWS = 25;

	public static final int CELL_WIDTH = 16;
	public static final int CELL_HEIGHT = 16;

	public static final int Y_OFFSET = (IMAGE_HEIGHT - (CELL_HEIGHT * ROWS) ) / 2;
	public static final int X_OFFSET = (IMAGE_WIDTH - (CELL_WIDTH * COLUMNS) ) / 2;

	public static void main( String[] args ) {
		BufferedImage bufferedImage = new BufferedImage( IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setColor( Color.white );
		g2d.fillRect( 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT );


		g2d.setColor( Color.BLACK );

		for(int x = 0; x < COLUMNS; x++){
			for(int y = 0; y < ROWS; y++){
				if(Math.random() > 0.5){
					g2d.drawRect( 	x * CELL_WIDTH + X_OFFSET,
							y * CELL_HEIGHT + Y_OFFSET,
							CELL_WIDTH, CELL_HEIGHT );
				}
				else{
					g2d.fillRect( 	x * CELL_WIDTH + X_OFFSET,
							y * CELL_HEIGHT + Y_OFFSET,
							CELL_WIDTH, CELL_HEIGHT );
				}

			}
		}

		try {
			ImageIO.write( bufferedImage, "png", new File( "test.png" ) );
			System.out.println( "Job's a gud'un." );
		}
		catch( IOException e){
			System.out.println( "Fatal error writing image: " + e.getMessage() );
			e.printStackTrace();
		}


//		try {
//			//Stage s = new Stage();
//			LoadDictionaryWindow window = new LoadDictionaryWindow();
//			window.launch( args );
//		}
//		catch(Exception e){
//
//		}
		new DictionaryLoaderView();


	}
}
