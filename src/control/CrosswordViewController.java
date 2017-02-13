package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.ApplicationState;
import model.PuzzleImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * CrosswordViewController.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
//TODO: Fix Comments
public class CrosswordViewController {
	private ApplicationController applicationController;
	private PuzzleImage puzzleImage;

	@FXML private Parent root;
	@FXML private Button regenerateButton;
	@FXML private Button saveButton;
	@FXML private Button newDictionaryButton;

	@FXML private ImageView answeredImage;
	@FXML private ImageView blankImage;

	@FXML public void saveButtonClicked( ActionEvent event){
		System.out.println( "Save Button Clicked" );

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter( "Image", "*.png" ) );

		fileChooser.setTitle("Save Answer Key To...");
		File answerKeyLocation = fileChooser.showSaveDialog( null );
		if (answerKeyLocation != null) {
			try {
				ImageIO.write( puzzleImage.getAnsweredImage(), "png", answerKeyLocation );
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

		fileChooser.setTitle("Save Blank Puzzle To...");
		File blankPuzzleLocation = fileChooser.showSaveDialog( null );
		if (blankPuzzleLocation != null) {
			try {
				ImageIO.write( puzzleImage.getBlankImage(), "png", blankPuzzleLocation );
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	@FXML public void regenerateButtonClicked( ActionEvent event){
		System.out.println( "Regenerate Button Clicked" );
		applicationController.generateCrossword();
	}

	@FXML public void newDictionaryButtonClicked( ActionEvent event){
		System.out.println( "New Dictionary Button Clicked" );
		applicationController.changeScene( ApplicationState.DICTIONARY_LOADER );
	}

	public void init( ApplicationController loader ){
		this.applicationController = loader;
	}

	public void loadImage( PuzzleImage puzzleImage ){
		this.puzzleImage = puzzleImage;

		try {
			answeredImage.setImage( new Image( "file:" + puzzleImage.getAnsweredImageFile().getPath() ) );
			blankImage.setImage( new Image( "file:" + puzzleImage.getBlankImageFile().getPath() ) );
		}
		catch( Exception e ){
			System.out.println( "unable to load image" );
		}
	}
}
