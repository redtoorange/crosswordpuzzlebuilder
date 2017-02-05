package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.DictionaryFile;

import java.io.File;

/**
 * DictionaryLoaderController.java - Control the JavaFX GUI that will allow the user to select a file to load.
 *
 * @author Andrew McGuiness
 * @version 27/Jan/2017
 */
public class DictionaryLoaderController {
	private final String FILE_CHOOSER_TITLE = "Selected a Dictionary File" ;
	private final String FILTER_DESCRIPTION = "Text File" ;
	private final String FILTER_EXTENSION = "*.txt";
	private final String INVALID_FILE_MSG = "That file is not valid.";

	private ApplicationController launchLoader;

	@FXML private Pane root;
	@FXML private Button generateButton;
	@FXML private Button fileChooserButton;
	@FXML private TextField filePathText;

	private FileChooser dictionaryChooser;
	private File selectedFile;

	public void init( ApplicationController loader ){
		launchLoader = loader;
		initChooser();
	}

	private void initChooser() {
		dictionaryChooser = new FileChooser( );
		dictionaryChooser.setTitle( FILE_CHOOSER_TITLE );

		FileChooser.ExtensionFilter filter =  new FileChooser.ExtensionFilter( FILTER_DESCRIPTION, FILTER_EXTENSION );
		dictionaryChooser.getExtensionFilters().add( filter );
	}

	@FXML public void generateButtonClicked( ActionEvent event){
		DictionaryFile dictionaryFile = new DictionaryFile( selectedFile );
		System.out.println( dictionaryFile );
		launchLoader.generateCrossword( dictionaryFile );
	}

	@FXML public void fileChooserButtonClicked( ActionEvent event){
		launchFileChooser( );
	}

	private void launchFileChooser() {
		File chosenFile = dictionaryChooser.showOpenDialog( null );

		if( chosenFile != null )
			processFile( chosenFile );
	}

	@FXML public void filePathTextEntered( ActionEvent event ){
		File enteredFile = new File( filePathText.getText() );

		if(enteredFile.exists())
			processFile( enteredFile );
	}

	private void processFile( File file ) {
		if( FileValidator.valid( file ) ) {
			selectedFile = file;
			filePathText.setText( selectedFile.getAbsolutePath( ) );
			enableGenerateButton( );
		}
		else {
			selectedFile = null;
			disableGenerateButton( );
			displayInvalidAlert( );
		}
	}

	private void enableGenerateButton(){
		generateButton.setDisable( false );
	}

	private void disableGenerateButton(){
		generateButton.setDisable( true );
	}

	private void displayInvalidAlert( ){
		Alert alert = new Alert( Alert.AlertType.ERROR, INVALID_FILE_MSG );
		alert.showAndWait();
	}
}
