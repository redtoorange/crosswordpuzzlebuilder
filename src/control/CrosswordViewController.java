package control;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.SceneType;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * CrosswordViewController.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
public class CrosswordViewController {
	private ApplicationController launchLoader;

	@FXML private Parent root;
	@FXML private Button regenerateButton;
	@FXML private Button saveButton;
	@FXML private Button newDictionaryButton;
	@FXML private ImageView imageView;

	@FXML public void saveButtonClicked( ActionEvent event){
		System.out.println( "Save Button Clicked" );

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");

		File file = fileChooser.showSaveDialog(null );
		if (file != null) {
			try {
				Image image = imageView.getImage();
				ImageIO.write( SwingFXUtils.fromFXImage(image,
						null), "png", file);
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	@FXML public void regenerateButtonClicked( ActionEvent event){
		System.out.println( "Regenerate Button Clicked" );
		launchLoader.regenerateCrossword();
	}

	@FXML public void newDictionaryButtonClicked( ActionEvent event){
		System.out.println( "New Dictionary Button Clicked" );
		launchLoader.swapScene( SceneType.DICTIONARY_LOADER );
	}

	public void init( ApplicationController loader ){
		this.launchLoader = loader;
	}

	public void loadImage( File file ){
		try {
			imageView.setImage( new Image( "file:" + file.getPath() ) );
		}
		catch( Exception e ){
			System.out.println( "unable to load image" );
		}
	}
}
