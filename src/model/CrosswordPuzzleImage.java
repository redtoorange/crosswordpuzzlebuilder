package model;

import javax.imageio.ImageIO;
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
	private BufferedImage blankImage;
	private BufferedImage answeredImage;

	private File blankImageFile = new File( "blank_temp.png"  );
	private File answeredImageFile = new File( "filled_temp.png" );

	public CrosswordPuzzleImage( BufferedImage blankImage, BufferedImage answeredImage ){
		this.blankImage = blankImage;
		this.answeredImage = answeredImage;
	}

	public void storeTempFiles(){
		try {
			ImageIO.write( blankImage, "png", blankImageFile );
			ImageIO.write( answeredImage, "png", answeredImageFile );

		}
		catch( IOException e){
			System.out.println( "Fatal error writing image: " + e.getMessage() );
			e.printStackTrace();
		}
	}

	public BufferedImage getBlankImage( ) {
		return blankImage;
	}

	public BufferedImage getAnsweredImage( ) {
		return answeredImage;
	}

	public File getBlankImageFile( ) {
		return blankImageFile;
	}

	public File getAnsweredImageFile( ) {
		return answeredImageFile;
	}

	public void setBlankImage( BufferedImage blankImage ) {
		this.blankImage = blankImage;
	}

	public void setAnsweredImage( BufferedImage answeredImage ) {
		this.answeredImage = answeredImage;
	}
}