package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.DictionaryFile;
import model.IncompleteWordException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * DictionaryLoaderController.java - Control the JavaFX GUI that will allow the user to select a file to load.
 *
 * @author Andrew McGuiness
 * @version 27/Jan/2017
 */
//TODO: Fix Comments
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

	/**
	 *	Hook this controller back to the application controller to allow the callback for when the generate button is clicked
	 * @param loader The main ApplicationController that handles application state and the controllers/views
	 */
	public void init( ApplicationController loader ){
		launchLoader = loader;
		initChooser();
	}

	/**
	 *	Initialize the file chooser and set it's filter and title.
	 */
	private void initChooser() {
		dictionaryChooser = new FileChooser( );
		dictionaryChooser.setTitle( FILE_CHOOSER_TITLE );

		FileChooser.ExtensionFilter filter =  new FileChooser.ExtensionFilter( FILTER_DESCRIPTION, FILTER_EXTENSION );
		dictionaryChooser.getExtensionFilters().add( filter );
	}

	/**
	 *	Event handler method for when the generate button is clicked.  It is disabled until a Valid file
	 *	is selected or typed in and processed.
	 * @param event Not Used
	 */
	@FXML public void generateButtonClicked( ActionEvent event){
		try{
			DictionaryFile dictionaryFile = new DictionaryFile( selectedFile );
			launchLoader.generateCrossword( dictionaryFile );
		}
		catch( IncompleteWordException iwe){
			displayInvalidAlert();
			generateButton.setDisable( true );
		}
		catch(FileNotFoundException e){
			displayInvalidAlert();
			generateButton.setDisable( true );
		}
	}

	/**
	 *	The FXML injected event handler for the file chooser button.
	 * @param event	Not Used
	 */
	@FXML public void fileChooserButtonClicked( ActionEvent event){
		launchFileChooser( );
	}

	/**
	 *	Launch the file chooser to select a dictionary file in the form of a .txt file.  If the user selects
	 *	a file, it is sent to be processed for validity (future proof for other file formats).
	 */
	private void launchFileChooser() {
		File chosenFile = null;
		try{
			chosenFile = dictionaryChooser.showOpenDialog( null );
		}
		catch ( NullPointerException npe){
			displayInvalidAlert( );	//Handles a bug where the user selects an icon and tries to load it
		}

		if( chosenFile != null ) {
			processFile( chosenFile );
		}
	}

	/**
	 * Injected method called by the FXML file when the text is entered into the TextField and the user presses
	 * enter.  The file is checked for existence and then processed for validity.
	 * @param event	Not used.
	 */
	@FXML public void filePathTextEntered( ActionEvent event ){
		File enteredFile = new File( filePathText.getText() );

		if( enteredFile.exists() )
			processFile( enteredFile );
		else{
			displayInvalidAlert();
		}
	}

	/**
	 *	Check that the file that was selected/type is actually valid.  If it is valid, enable the generate button.
	 *	This is just a simple extension check for the file, it does not parse the contents.  The contents are parsed
	 * when the file is loaded by the dictionary loader.  If the contents are invalid, then a warning is displayed again.
	 *  @param file	The selected or typed file to check.
	 */
	private void processFile( File file ) {
		if( FileValidator.valid( file ) ) {
			selectedFile = file;
			filePathText.setText( selectedFile.getAbsolutePath( ) );
			generateButton.setDisable( false );
		}
		else {
			selectedFile = null;
			generateButton.setDisable( true );
			displayInvalidAlert( );
		}
	}

	/**
	 *	Helper function to display a warning message when there is an error loading the file.
	 */
	private void displayInvalidAlert( ){
		Alert alert = new Alert( Alert.AlertType.ERROR, INVALID_FILE_MSG );
		alert.showAndWait();
	}
}
