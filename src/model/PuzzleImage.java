package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * PuzzleImage.java - Model Representing a Puzzle Image.  A puzzle image is formed from two parts, an answer key and a
 * blank puzzle.  Both should be stored to the disk before being used in a view (JavaFX required an image file on the disk
 * to use an ImageViewer).
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
//TODO: Fix Comments
public class PuzzleImage {

	private BufferedImage blankImage;
	private BufferedImage answeredImage;

	private File blankImageFile = new File( "blank_temp.png"  );
	private File answeredImageFile = new File( "filled_temp.png" );

	/**
	 * Create a new PuzzleImage and populate the two BufferedImages that represent this puzzle completely.
	 * @param blankImage	The blank puzzle image (no answers)
	 * @param answeredImage The filled puzzle image (answered)
	 */
	public PuzzleImage( BufferedImage blankImage, BufferedImage answeredImage ){
		this.blankImage = blankImage;
		this.answeredImage = answeredImage;
	}

	/**
	 * For the Image Viewers to work, the BufferedImages need to be stored to disk.  This method writes both
	 * BufferedImaged to the disk as temp files.
	 */
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

	/**
	 * Get the BufferedImage form of the Blank Image.
	 * @return BufferedImage that is blank.
	 */
	public BufferedImage getBlankImage( ) {
		return blankImage;
	}

	/**
	 * Get the BufferedImage form of the Answered Image.
	 * @return BufferedImage that is filled in.
	 */
	public BufferedImage getAnsweredImage( ) {
		return answeredImage;
	}

	/**
	 * Accessor to located the temp File on disk of the blank image file.  This is is used by JavaFX ImageViewer
	 * @return A .PNG file that is stored locally and purged when the program closes.
	 */
	public File getBlankImageFile( ) {
		return blankImageFile;
	}

	/**
	 * Accessor to located the temp file on disk of the answered image file.  This is used by JavaFX ImageViewer.
	 * @return A .PNG file that is stored locally and purged when the program closes.
	 */
	public File getAnsweredImageFile( ) {
		return answeredImageFile;
	}

	/**
	 * Deletes the temp files that are stored on disk.  This is called when the application closes or when a new puzzle image is generated.
	 */
	public void cleanup(){
		if( blankImageFile.exists() )
			blankImageFile.delete();
		if( answeredImageFile.exists() )
			answeredImageFile.delete();
	}
}