package control;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ApplicationState;
import model.DictionaryFile;
import model.Grid;

/**
 * ApplicationController.java - Main Controller class for the {@link Application}.  Controls {@link Scene} switching and message
 * passing between the Primary Controllers, {@link DictionaryLoaderController} and {@link CrosswordViewController}.
 *
 * @author Andrew McGuiness
 * @version 14/Feb/2017
 * @see DictionaryLoaderController
 * @see CrosswordViewController
 * @see GridController
 * @see PuzzleImageController
 */
public class ApplicationController extends Application {
	/**
	 * {@link DictionaryLoaderController} that is used to control the view for loading {@link DictionaryFile}s.
	 */
	private DictionaryLoaderController dictionaryLoaderController;
	/**
	 * {@link CrosswordViewController} that is control the CrosswordPuzzle view, which is used to view the
	 * {@link model.PuzzleImage}s genetated with the {@link PuzzleImageController}.
	 */
	private CrosswordViewController crosswordViewController;
	/**
	 * {@link GridController} that is used to build a {@link Grid} from the {@link DictionaryFile}.
	 */
	private GridController gridController;
	/**
	 * {@link PuzzleImageController} that is used to build a {@link model.PuzzleImage} from the {@link Grid}.
	 */
	private PuzzleImageController puzzleImageController;


	/**
	 * {@link Scene} view loaded in with a {@link FXMLLoader}.  Used to load in a {@link DictionaryFile}.
	 */
	private Scene dictionaryScene;
	/**
	 * {@link Scene} view loaded in with a {@link FXMLLoader}.  Used to view the {@link model.PuzzleImage}s.
	 */
	private Scene viewerScene;

	/**
	 * {@link Stage} passed in by the system.  Is the primary window handle.
	 */
	private Stage mainStage;
	/**
	 * {@link DictionaryFile} that contains a {@link model.WordList} that is used to generate a {@link Grid}.
	 */
	private DictionaryFile currentDictionaryFile;


	public static void main( String[] args ) {
		launch( args );
	}

	/**
	 * Called by the system to Start the {@link ApplicationController}.  This will initialize the view and contoler
	 * references.  It then changes the view to the DictionaryLoaderView, which is the default.
	 *
	 * @param primaryStage {@link Stage} passed in by the system when the {@link ApplicationController} launches.
	 * @throws Exception	Default exception thrown by the system if the Window fails to launch.
	 */
	@Override
	public void start( Stage primaryStage ) throws Exception {
		mainStage = primaryStage;

		initDictionaryLoader( );
		initCrosswordViewer( );

		puzzleImageController = new PuzzleImageController( );
		gridController = new GridController( );

		primaryStage.setTitle( "Crossword Generator 2K17" );
		changeScene( ApplicationState.DICTIONARY_LOADER );
	}

	/**
	 * Initialize the DictionaryLoaderView and the {@link DictionaryLoaderController}.
	 *
	 * @throws java.io.IOException	if the {@link FXMLLoader} fails to find the FXML file.
	 */
	private void initDictionaryLoader() throws java.io.IOException {
		FXMLLoader dictionay = new FXMLLoader( getClass( ).getResource( "../view/DictionaryLoader.fxml" ) );
		dictionaryScene = new Scene( dictionay.load( ) );

		dictionaryLoaderController = dictionay.getController( );
		dictionaryLoaderController.init( this );
	}

	/**
	 * Initialize the CrosswordViewerScene and the {@link CrosswordViewController}.
	 *
	 * @throws java.io.IOException if the {@link FXMLLoader} fails to find the FXML file.
	 */
	private void initCrosswordViewer() throws java.io.IOException {
		FXMLLoader viewer = new FXMLLoader( getClass( ).getResource( "../view/CrosswordView.fxml" ) );
		viewerScene = new Scene( viewer.load( ) );

		crosswordViewController = viewer.getController( );
		crosswordViewController.init( this );
	}


	/**
	 * Used by the ViewControllers to switch the view of the application.
	 *
	 * @param applicationState What {@link ApplicationState} to switch the Application to.
	 */
	public void changeScene( ApplicationState applicationState ) {
		mainStage.hide( );

		switch ( applicationState ) {
			case IMAGE_VIEWER:
				mainStage.setScene( viewerScene );
				break;
			case DICTIONARY_LOADER:
			default:
				mainStage.setScene( dictionaryScene );
				break;
		}

		mainStage.sizeToScene( );
		mainStage.show( );
	}

	/**
	 * Set the {@link DictionaryFile} to then call the {@link #generateCrossword(DictionaryFile)} method.
	 *
	 * @param dictionaryFile {@link DictionaryFile} to set the current {@link DictionaryFile} to and use for generation.
	 */
	public void generateCrossword( DictionaryFile dictionaryFile ) {
		this.currentDictionaryFile = dictionaryFile;
		generateCrossword( );
	}

	/**
	 * Generate a {@link Grid} using the {@link GridController}, then use that to create a {@link model.PuzzleImage} using the
	 * {@link PuzzleImageController} and display it using the {@link CrosswordViewController}.
	 */
	public void generateCrossword() {
		currentDictionaryFile.reset( );

		Grid grid = gridController.createGrid( currentDictionaryFile );

		crosswordViewController.loadImage( puzzleImageController.createPuzzleImage( grid, 50 ) );
		changeScene( ApplicationState.IMAGE_VIEWER );
	}
}