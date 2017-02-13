package control;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ApplicationState;
import model.DictionaryFile;
import model.Grid;

/**
 * ApplicationController.java - Main Controller class for the JavaFX Application.  Controls scene switching and message
 * passing between Primary Controllers.
 *
 * @author Andrew J. McGuiness
 * @version 27/Jan/2017
 */
public class ApplicationController extends Application {
	//Primary Controllers
	private DictionaryLoaderController dictionaryLoaderController;
	private CrosswordViewController crosswordViewController;
	private GridController gridController;
	private PuzzleImageController puzzleImageController;

	//Scene references
	private Scene dictionaryScene;
	private Scene viewerScene;

	//primaryStage passed in by JavaFX
	private Stage mainStage;

	private DictionaryFile currentDictionaryFile;


	public static void main( String[] args ) {
		launch( args );
	}

	/**
	 * Called by the system to Start the application.  This will initialize the view and contoller references.  It then
	 * changes the view to the DictionaryLoaderView, which is the default.
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start( Stage primaryStage ) throws Exception {
		mainStage = primaryStage;

		initDictionaryLoader( );
		initCrosswordViewer( );

		puzzleImageController = new PuzzleImageController();
		gridController = new GridController();

		primaryStage.setTitle( "Crossword Generator 2K17" );
		changeScene( ApplicationState.DICTIONARY_LOADER );
	}

	/**
	 * Intialize the DictionaryLoaderView and it's controller reference
	 * @throws java.io.IOException
	 */
	private void initDictionaryLoader() throws java.io.IOException {
		FXMLLoader dictionay = new FXMLLoader( getClass().getResource("../view/DictionaryLoader.fxml") );
		dictionaryScene = new Scene( dictionay.load() );

		dictionaryLoaderController = dictionay.getController();
		dictionaryLoaderController.init( this );
	}

	/**
	 * Initialize the CrosswordView and it's controller reference
	 * @throws java.io.IOException
	 */
	private void initCrosswordViewer() throws java.io.IOException {
		FXMLLoader viewer = new FXMLLoader( getClass().getResource( "../view/CrosswordView.fxml" ) );
		viewerScene = new Scene( viewer.load() );

		crosswordViewController = viewer.getController();
		crosswordViewController.init( this );
	}


	/**
	 * A method used by the ViewControllers to switch that view of the application.
	 * @param applicationState What state to switch the Application to.
	 */
	public void changeScene( ApplicationState applicationState ){
		mainStage.hide();

		switch( applicationState){
			case IMAGE_VIEWER:
				mainStage.setScene( viewerScene );
				break;
			case DICTIONARY_LOADER:default:
				mainStage.setScene( dictionaryScene );
				break;
		}

		mainStage.sizeToScene();
		mainStage.show();
	}

	/**
	 * Method to generate a new Crossword and display it using the CrosswordImageView
	 * @param dictionaryFile The DictionaryFile that should be used in the Crossword Generation
	 */
	public void generateCrossword( DictionaryFile dictionaryFile ){
		this.currentDictionaryFile = dictionaryFile;
		generateCrossword();
	}

	/**
	 * Method to generate a new Crossword and display it using the CrosswordImageView
	 */
	public void generateCrossword(){
		currentDictionaryFile.reset();

		Grid grid = gridController.createGrid( currentDictionaryFile );

		crosswordViewController.loadImage( puzzleImageController.createPuzzleImage( grid, 50 ) );
		changeScene( ApplicationState.IMAGE_VIEWER );
	}
}