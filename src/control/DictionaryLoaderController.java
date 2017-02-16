package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.DictionaryFile;
import model.IncompleteWordException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * DictionaryLoaderController.java - Control the JavaFX GUI that will allow the user to select a {@link File} to load
 * with a {@link FileChooser}.
 *
 * @author Andrew McGuiness
 * @version 14/Feb/2017
 */
public class DictionaryLoaderController {
	/** The {@link String} title that will be displayed on the {@link FileChooser} when loading a {@link DictionaryFile}. */
	private final String FILE_CHOOSER_TITLE = "Selected a Dictionary File";

	/** The {@link String} description of the {@link java.io.FileFilter} set in the {@link FileChooser}. */
	private final String FILTER_DESCRIPTION = "Text File";

	/** The actual {@link String} filter of the {@link java.io.FileFilter} set in the {@link FileChooser}. */
	private final String FILTER_EXTENSION = "*.txt";

	/** The {@link String} message to display to the user on the {@link Alert} message. */
	private final String INVALID_FILE_MSG = "That file is not valid.";

	/** The main {@link ApplicationController} that controls the entire program. */
	private ApplicationController launchLoader;

	/** The reference to a {@link FileChooser} that is used to select a {@link DictionaryFile}. */
	private FileChooser dictionaryChooser;

	/** The raw {@link File} that is loaded in by the {@link FileChooser}. */
	private File selectedFile;

	/** The {@link Button} that generates a new {@link model.Grid} from a loaded {@link DictionaryFile}. */
	@FXML
	private Button generateButton;

	/** The {@link TextField} that will display the path to the selected {@link File} or can be used for input. */
	@FXML
	private TextField filePathText;


	/**
	 * Hook this controller back to the {@link ApplicationController} to allow the callback for when the generate
	 * button is clicked.
	 *
	 * @param loader The main {@link ApplicationController} that handles {@link model.ApplicationState} and the
	 *               controllers/views.
	 */
	public void init( ApplicationController loader ) {
		launchLoader = loader;
		initChooser( );
	}

	/**
	 * Initialize the {@link FileChooser} and set it's {@link java.io.FileFilter} and title.
	 */
	private void initChooser() {
		dictionaryChooser = new FileChooser( );
		dictionaryChooser.setTitle( FILE_CHOOSER_TITLE );

		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter( FILTER_DESCRIPTION, FILTER_EXTENSION );
		dictionaryChooser.getExtensionFilters( ).add( filter );
	}

	/**
	 * {@link ActionEvent} Listener method for when the generate button is clicked.  It is disabled until a Valid {@link
	 * File} is selected or typed in and processed.
	 *
	 * @param event Not Used.
	 */
	@FXML
	public void generateButtonClicked( ActionEvent event ) {
		try {
			DictionaryFile dictionaryFile = new DictionaryFile( selectedFile );
			launchLoader.generateCrossword( dictionaryFile );
		} catch ( IncompleteWordException iwe ) {
			displayInvalidAlert( );
			generateButton.setDisable( true );
		} catch ( FileNotFoundException e ) {
			displayInvalidAlert( );
			generateButton.setDisable( true );
		}
	}

	/**
	 * {@link ActionEvent} Listener for the file chooser button.  Launches a {@link FileChooser} to select a {@link
	 * DictionaryFile} to load from disk.
	 *
	 * @param event Not Used.
	 */
	@FXML
	public void fileChooserButtonClicked( ActionEvent event ) {
		launchFileChooser( );
	}

	/**
	 * Launch the {@link FileChooser} to select a {@link DictionaryFile} in the form of a .txt file.  If the user
	 * selects a {@link File}, it is sent to {@link #processFile(File)}. (future proof for other file formats).
	 */
	private void launchFileChooser() {
		File chosenFile = null;

		try {
			chosenFile = dictionaryChooser.showOpenDialog( null );
		} catch ( NullPointerException npe ) {
			displayInvalidAlert( );    //Handles a bug where the user selects an icon and tries to load it
		}

		if ( chosenFile != null )
			processFile( chosenFile );
	}

	/**
	 * {@link ActionEvent} Listener for when the user types into the {@link TextField}.  The {@link File} is checked for
	 * existence and then processed for validity.
	 *
	 * @param event Not used.
	 */
	@FXML
	public void filePathTextEntered( ActionEvent event ) {
		File enteredFile = new File( filePathText.getText( ) );

		if ( enteredFile.exists( ) )
			processFile( enteredFile );
		else
			displayInvalidAlert( );
	}

	/**
	 * Ensure that the {@link File} has the correct extension, then enable the generate button.
	 *
	 * @param file The selected or typed {@link File} to process.
	 */
	private void processFile( File file ) {
		if ( valid( file ) ) {
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
	 * Helper function to display an {@link Alert} message when there is an error loading the file.
	 */
	private void displayInvalidAlert() {
		Alert alert = new Alert( Alert.AlertType.ERROR, INVALID_FILE_MSG );
		alert.showAndWait( );
	}

	/**
	 * Helper function that ensures the extension of the {@link File} is ".txt"
	 *
	 * @param file The {@link File} to attempt to validate.
	 * @return True if the extension of the {@link File} is ".txt", false otherwise.
	 */
	private boolean valid( File file ) {
		boolean validFile = true;

		if ( !file.getAbsoluteFile( ).toString( ).endsWith( ".txt" ) )
			validFile = false;

		return validFile;
	}
}
