package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.ApplicationState;
import model.PuzzleImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * CrosswordViewController.java - The controller for the CrosswordView.  JavaFX injects this controller into the CrosswordView
 * automatically.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 */
public class CrosswordViewController {

	/**
	 * The main {@link ApplicationController} that is used to control the entire Application.
	 */
	private ApplicationController applicationController;
	/**
	 * The currently loaded {@link PuzzleImage}, used to save the {@link File}s to disk.
	 */
	private PuzzleImage puzzleImage;
	/**
	 * {@link ImageView} that displays the answered {@link BufferedImage} from the {@link PuzzleImage}.
	 */
	@FXML
	private ImageView answeredImage;
	/**
	 * {@link ImageView} that displays the blank {@link BufferedImage} from the {@link PuzzleImage}.
	 */
	@FXML
	private ImageView blankImage;
	/**
	 * A cached reference for a {@link FileChooser} that is used for saving files to the disk.
	 */
	private FileChooser fileChooser;

	/**
	 * {@link ActionEvent} Listener that is called when the save button is clicked on the CrossWordView.
	 *
	 * @param event Not Used.
	 */
	@FXML
	public void saveButtonClicked( ActionEvent event ) {
		fileChooser.setTitle( "Save Answer Key To..." );
		writeImageFile( fileChooser.showSaveDialog( null ), puzzleImage.getAnsweredImage( ) );

		fileChooser.setTitle( "Save Blank Puzzle To..." );
		writeImageFile( fileChooser.showSaveDialog( null ), puzzleImage.getBlankImage( ) );
	}

	/**
	 * Write a {@link BufferedImage} to the file chosen with a {@link FileChooser}.
	 *
	 * @param file          {@link File} that the {@link BufferedImage} should be written to.
	 * @param bufferedImage The {@link BufferedImage} pulled from a {@link PuzzleImage}.
	 */
	private void writeImageFile( File file, BufferedImage bufferedImage ) {
		if ( file != null ) {
			try {
				ImageIO.write( bufferedImage, "png", file );
			} catch ( IOException ex ) {
				System.out.println( ex.getMessage( ) );
			}
		}
	}

	/**
	 * {@link ActionEvent} Listener that is called when the regenerate button is clicked on the CrossWordView.
	 *
	 * @param event Not Used.
	 */
	@FXML
	public void regenerateButtonClicked( ActionEvent event ) {
		applicationController.generateCrossword( );
	}

	/**
	 * {@link ActionEvent} Listener that is called when the new dictionary button is clicked on the CrossWordView.
	 *
	 * @param event Not Used.
	 */
	@FXML
	public void newDictionaryButtonClicked( ActionEvent event ) {
		applicationController.changeScene( ApplicationState.DICTIONARY_LOADER );
	}

	/**
	 * Called by the {@link ApplicationController} when it is initializing.  Stand-in for a normal constructor.
	 *
	 * @param loader Used as a callback to switch the {@link javafx.scene.Scene}
	 */
	public void init( ApplicationController loader ) {
		this.applicationController = loader;

		fileChooser = new FileChooser( );
		fileChooser.getExtensionFilters( ).add( new FileChooser.ExtensionFilter( "Image", "*.png" ) );
	}

	/**
	 * Loads a {@link PuzzleImage}'s image {@link File}s from disk into the two {@link ImageView}s.
	 *
	 * @param puzzleImage The {@link PuzzleImage} to use to access the {@link File} images.
	 */
	public void loadImage( PuzzleImage puzzleImage ) {
		this.puzzleImage = puzzleImage;

		try {
			answeredImage.setImage( new Image( "file:" + puzzleImage.getAnsweredImageFile( ).getPath( ) ) );
			blankImage.setImage( new Image( "file:" + puzzleImage.getBlankImageFile( ).getPath( ) ) );
		} catch ( Exception e ) {
			System.out.println( "unable to load image" );
		}
	}
}
