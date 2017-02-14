package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * PuzzleImage.java - A Model Representing a pair of {@link BufferedImage}s with their {@link File}s saved locally.
 * Both must be saved to the disk before being used in the {@link javafx.scene.image.ImageView}.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 */
public class PuzzleImage {
	/**
	 * The {@link BufferedImage} that is written to by the {@link control.PuzzleImageController}.  It is saved to the disk
	 * by the {@link control.CrosswordViewController} when the user selects save.
	 */
	private BufferedImage blankImage;
	/**
	 * The {@link BufferedImage} that is written to by the {@link control.PuzzleImageController}.  It is saved to the disk
	 * by the {@link control.CrosswordViewController} when the user selects save.
	 */
	private BufferedImage answeredImage;

	/**
	 * The local {@link File} that is saved inside the Application's folder.  This allows the {@link control.CrosswordViewController}
	 * to load the {@link File} into an {@link javafx.scene.image.ImageView} so the user can review it.
	 */
	private File blankImageFile = new File( "blank_temp.png" );
	/**
	 * The local {@link File} that is saved inside the Application's folder.  This allows the {@link control.CrosswordViewController}
	 * to load the {@link File} into an {@link javafx.scene.image.ImageView} so the user can review it.
	 */
	private File answeredImageFile = new File( "filled_temp.png" );

	/**
	 * Create a new {@link PuzzleImage} and populate the two {@link BufferedImage}s that represent this puzzle completely.
	 *
	 * @param blankImage    The blank puzzle {@link BufferedImage} (no answers)
	 * @param answeredImage The filled puzzle {@link BufferedImage} (answered)
	 */
	public PuzzleImage( BufferedImage blankImage, BufferedImage answeredImage ) {
		this.blankImage = blankImage;
		this.answeredImage = answeredImage;
	}

	/**
	 * For the {@link javafx.scene.image.ImageView} to work, the {@link BufferedImage}s need to be stored to disk.  This method writes both
	 * {@link BufferedImage} to the disk as temp {@link File}.
	 */
	public void storeTempFiles() {
		try {
			ImageIO.write( blankImage, "png", blankImageFile );
			ImageIO.write( answeredImage, "png", answeredImageFile );
		} catch ( IOException e ) {
			System.out.println( "Fatal error writing image: " + e.getMessage( ) );
			e.printStackTrace( );
		}
	}

	/**
	 * Get the {@link BufferedImage} form of the Blank Image.
	 *
	 * @return {@link BufferedImage} that is blank.
	 */
	public BufferedImage getBlankImage() {
		return blankImage;
	}

	/**
	 * Get the {@link BufferedImage} form of the Answered Image.
	 *
	 * @return {@link BufferedImage} that is filled in.
	 */
	public BufferedImage getAnsweredImage() {
		return answeredImage;
	}

	/**
	 * Accessor to located the temp {@link File} on disk of the blank {@link BufferedImage} .  This is is used by
	 * {@link javafx.scene.image.ImageView} inside of the {@link control.CrosswordViewController}.
	 *
	 * @return A .PNG {@link File} that is stored locally and purged when the program closes.
	 */
	public File getBlankImageFile() {
		return blankImageFile;
	}

	/**
	 * Accessor to located the temp {@link File} on disk of the answered {@link BufferedImage} .  This is used by
	 * {@link javafx.scene.image.ImageView} inside of the {@link control.CrosswordViewController}.
	 *
	 * @return A .PNG {@link File} that is stored locally and purged when the program closes.
	 */
	public File getAnsweredImageFile() {
		return answeredImageFile;
	}

	/**
	 * Deletes the temp {@link File} that are stored on disk.  This is called when the Application closes or when a new
	 * puzzle image is generated.
	 */
	public void cleanup() {
		if ( blankImageFile.exists( ) )
			blankImageFile.delete( );
		if ( answeredImageFile.exists( ) )
			answeredImageFile.delete( );
	}
}